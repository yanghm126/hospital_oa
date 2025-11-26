package com.ruoyi.hr.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.domain.HrAuditApply;
import com.ruoyi.hr.mapper.HrAttendanceRecordMapper;
import com.ruoyi.hr.mapper.HrAuditApplyMapper;
import com.ruoyi.hr.service.IHrAuditApplyService;

/**
 * 审批申请Service业务层处理
 */
@Service
public class HrAuditApplyServiceImpl extends ServiceImpl<HrAuditApplyMapper, HrAuditApply> implements IHrAuditApplyService
{
    @Autowired
    private ISysDeptService deptService;
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private HrAuditApplyMapper applyMapper;
    
    @Autowired
    private HrAttendanceRecordMapper recordMapper;

    @Override
    public List<HrAuditApply> selectApplyList(HrAuditApply apply)
    {
        QueryWrapper<HrAuditApply> qw = new QueryWrapper<>();
        if (apply.getUserId() != null) {
            qw.eq("a.user_id", apply.getUserId());
        }
        if (apply.getAuditorId() != null) {
            qw.eq("a.auditor_id", apply.getAuditorId());
        }
        if (StringUtils.isNotEmpty(apply.getStatus())) {
            qw.eq("a.status", apply.getStatus());
        }
        qw.orderByDesc("a.create_time");
        return applyMapper.selectApplyList(qw);
    }

    @Override
    @Transactional
    public boolean submitApply(HrAuditApply apply)
    {
        // 1. 补全信息
        Long userId = SecurityUtils.getUserId();
        Long deptId = SecurityUtils.getDeptId();
        apply.setUserId(userId);
        apply.setDeptId(deptId);
        apply.setCreateTime(DateUtils.getNowDate());
        apply.setStatus("0"); // 待审
        
        // 2. 查找审批人 (SysDept Leader)
        SysDept dept = deptService.selectDeptById(deptId);
        if (dept != null && StringUtils.isNotEmpty(dept.getLeader())) {
            String leaderName = dept.getLeader();
            // 假设 Leader 存的是用户名或昵称，需反查ID
            // 生产环境建议 sys_dept 增加 leader_id 字段
            SysUser leaderUser = userService.selectUserByUserName(leaderName);
            if (leaderUser == null) {
                 // 尝试查昵称? 这里假设是userName
                 // 如果找不到，默认给自己审核(测试用)或抛异常
                 // throw new ServiceException("未找到部门负责人: " + leaderName);
                 // Fallback: 如果找不到，暂且设为 admin (ID=1)
                 apply.setAuditorId(1L);
            } else {
                apply.setAuditorId(leaderUser.getUserId());
            }
        } else {
            // 无部门负责人，默认转给超管
            apply.setAuditorId(1L);
        }
        
        return save(apply);
    }

    @Override
    @Transactional
    public boolean auditApply(Long applyId, String status, String msg)
    {
        HrAuditApply apply = getById(applyId);
        if (apply == null) {
            throw new ServiceException("申请不存在");
        }
        if (!"0".equals(apply.getStatus())) {
            throw new ServiceException("该申请已审核");
        }
        
        // 校验当前用户是否是审核人 (略，Controller层鉴权)
        
        apply.setStatus(status);
        apply.setAuditMsg(msg);
        apply.setAuditTime(DateUtils.getNowDate());
        
        boolean success = updateById(apply);
        
        // 3. 审核通过后的回调
        if (success && "1".equals(status)) {
            if ("1".equals(apply.getApplyType())) {
                processLeavePass(apply);
            } else if ("2".equals(apply.getApplyType())) {
                processMissedPunchPass(apply);
            }
        }
        
        return success;
    }
    
    /**
     * 处理请假通过 -> 写入考勤记录
     */
    private void processLeavePass(HrAuditApply apply) {
        // 简单逻辑：在 hr_attendance_record 插入一条 "请假" 记录
        // 也可以遍历每一天
        HrAttendanceRecord record = new HrAttendanceRecord();
        record.setUserId(apply.getUserId());
        record.setCheckTime(apply.getStartTime()); // 记录为开始时间
        record.setCheckType("1"); // 上班
        record.setResult("6"); // 假设 6=请假
        record.setRemark("请假审批通过: " + apply.getReason());
        recordMapper.insert(record);
    }
    
    /**
     * 处理补卡通过
     */
    private void processMissedPunchPass(HrAuditApply apply) {
        HrAttendanceRecord record = new HrAttendanceRecord();
        record.setUserId(apply.getUserId());
        record.setCheckTime(apply.getStartTime()); // 补卡时间
        record.setCheckType("1"); // 需前端区分补上班还是下班，暂默认上班
        record.setResult("1"); // 补卡视为正常
        record.setRemark("补卡审批通过: " + apply.getReason());
        recordMapper.insert(record);
    }
}

