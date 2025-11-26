package com.ruoyi.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.hr.domain.HrAttendanceGroup;

/**
 * 考勤组Service接口
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
public interface IHrAttendanceGroupService extends IService<HrAttendanceGroup>
{
    /**
     * 根据部门ID查询考勤组
     * 
     * @param deptId 部门ID
     * @return 考勤组
     */
    public HrAttendanceGroup selectAttendanceGroupByDeptId(Long deptId);
}

