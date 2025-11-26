package com.ruoyi.hr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.hr.domain.HrAttendanceGroup;
import com.ruoyi.hr.mapper.HrAttendanceGroupMapper;
import com.ruoyi.hr.service.IHrAttendanceGroupService;

/**
 * 考勤组Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Service
public class HrAttendanceGroupServiceImpl extends ServiceImpl<HrAttendanceGroupMapper, HrAttendanceGroup> implements IHrAttendanceGroupService
{
    @Autowired
    private HrAttendanceGroupMapper attendanceGroupMapper;

    /**
     * 根据部门ID查询考勤组
     * 
     * @param deptId 部门ID
     * @return 考勤组
     */
    @Override
    public HrAttendanceGroup selectAttendanceGroupByDeptId(Long deptId)
    {
        return attendanceGroupMapper.selectByDeptId(deptId);
    }
}

