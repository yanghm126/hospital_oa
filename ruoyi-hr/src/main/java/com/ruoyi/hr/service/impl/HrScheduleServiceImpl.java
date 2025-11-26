package com.ruoyi.hr.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.hr.domain.HrEmployee;
import com.ruoyi.hr.domain.HrSchedule;
import com.ruoyi.hr.domain.ScheduleMatrixDTO;
import com.ruoyi.hr.mapper.HrScheduleMapper;
import com.ruoyi.hr.service.IHrEmployeeService;
import com.ruoyi.hr.service.IHrScheduleService;

/**
 * 排班Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Service
public class HrScheduleServiceImpl extends ServiceImpl<HrScheduleMapper, HrSchedule> implements IHrScheduleService
{
    @Autowired
    private IHrEmployeeService hrEmployeeService;

    /**
     * 查询排班矩阵
     */
    @Override
    public List<ScheduleMatrixDTO> selectScheduleMatrix(Long deptId, String month)
    {
        // 1. 查询该部门下的所有员工 (关联sys_user)
        // 为了方便，先查HrEmployee列表（需确保HrEmployeeMapper.selectEmployeeList支持deptId过滤）
        // 这里我们假设通过HrEmployeeService能查到
        HrEmployee queryEmp = new HrEmployee();
        // 注意：实际开发中需要在HrEmployee中添加deptId字段或通过XML关联查询过滤
        // 这里暂时模拟查询所有，生产环境需严格按deptId过滤
        List<HrEmployee> empList = hrEmployeeService.selectEmployeeList(queryEmp);
        
        // 过滤部门 (如果Mapper没写dept过滤的话)
        if (deptId != null) {
            empList = empList.stream()
                .filter(e -> e.getSysUser() != null && deptId.equals(e.getSysUser().getDeptId()))
                .collect(Collectors.toList());
        }

        // 2. 查询该月的所有排班记录
        QueryWrapper<HrSchedule> scheduleQuery = new QueryWrapper<>();
        if (deptId != null) {
            scheduleQuery.eq("dept_id", deptId);
        }
        scheduleQuery.apply("DATE_FORMAT(work_date, '%Y-%m') = {0}", month);
        List<HrSchedule> scheduleList = list(scheduleQuery);

        // 3. 组装矩阵
        List<ScheduleMatrixDTO> matrix = new ArrayList<>();
        
        // 转Map方便查找: Key=userId_day, Value=shiftId
        Map<String, Long> scheduleMap = new HashMap<>();
        for (HrSchedule s : scheduleList) {
            String day = DateUtils.parseDateToStr("dd", s.getWorkDate());
            scheduleMap.put(s.getUserId() + "_" + day, s.getShiftId());
        }

        for (HrEmployee emp : empList) {
            if (emp.getSysUser() == null) continue;
            
            ScheduleMatrixDTO dto = new ScheduleMatrixDTO();
            dto.setUserId(emp.getUserId());
            dto.setUserName(emp.getSysUser().getNickName());
            dto.setDeptName(emp.getSysUser().getDept() != null ? emp.getSysUser().getDept().getDeptName() : "");
            
            Map<String, Long> userSchedule = new HashMap<>();
            // 遍历1-31号 (虽然只要存在的就行，但前端可能需要完整map，这里只放存在的，前端根据日期key取)
            // 优化：只放入有排班的日期
            for (int i = 1; i <= 31; i++) {
                String day = String.format("%02d", i);
                String key = emp.getUserId() + "_" + day;
                if (scheduleMap.containsKey(key)) {
                    userSchedule.put(day, scheduleMap.get(key));
                }
            }
            dto.setScheduleMap(userSchedule);
            matrix.add(dto);
        }

        return matrix;
    }

    /**
     * 批量保存排班
     */
    @Override
    @Transactional
    public boolean saveScheduleBatch(List<HrSchedule> scheduleList)
    {
        if (scheduleList == null || scheduleList.isEmpty()) return true;
        
        // 策略：根据 unique key (user_id, work_date) 进行 Upsert
        // MyBatis Plus 的 saveOrUpdateBatch 依赖主键ID，如果前端没传ID则会Insert
        // 这里的场景是：如果已存在则更新，不存在则插入。
        // 最好是先删除该批次日期的旧记录，再插入新记录？或者利用 ON DUPLICATE KEY UPDATE
        // 为简单起见，我们使用 "先删后插" (针对单个用户单天) 性能较差，
        // 或者使用 MP 的 saveOrUpdate (需要先查一下是否存在)
        
        // 优化策略：前端传回来的数据通常是整月的改动
        // 我们可以先根据 ID 判断。如果 scheduleId 有值，则 Update；无值则 Insert。
        // 但前端可能不知道 ID。
        
        // 最终策略：尝试根据 user_id + work_date 查询 ID，填入后再 saveOrUpdateBatch
        // 这在数据量大时慢。
        // 更高效：Delete where user_id in (...) and work_date in (...) -> Insert
        
        // 这里采用 MP saveOrUpdateBatch，前提是最好能查出ID。
        // 考虑到复杂度，这里写一个简单的循环处理（生产环境建议优化为批量SQL）
        
        List<HrSchedule> toSave = new ArrayList<>();
        
        for (HrSchedule s : scheduleList) {
            QueryWrapper<HrSchedule> qw = new QueryWrapper<>();
            qw.eq("user_id", s.getUserId());
            qw.eq("work_date", DateUtils.parseDateToStr("yyyy-MM-dd", s.getWorkDate()));
            HrSchedule exist = getOne(qw);
            if (exist != null) {
                s.setScheduleId(exist.getScheduleId());
            }
            toSave.add(s);
        }
        
        return saveOrUpdateBatch(toSave);
    }
}

