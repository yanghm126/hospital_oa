package com.ruoyi.hr.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.ClockInBody;
import java.util.Map;

/**
 * 考勤业务 Service 接口
 */
public interface IAttendanceService
{
    /**
     * 移动端打卡
     * @param userId 用户ID
     * @param deptId 部门ID
     * @param body 打卡参数
     * @return 结果
     */
    AjaxResult clockIn(Long userId, Long deptId, ClockInBody body);

    /**
     * 个人月报统计
     * @param userId 用户ID
     * @param month 月份 (yyyy-MM)
     * @return 统计结果
     */
    Map<String, Object> getPersonalMonthlyStats(Long userId, String month);

    /**
     * 科室异常统计
     * @param deptId 部门ID
     * @param month 月份 (yyyy-MM)
     * @return 统计结果
     */
    Map<String, Object> getDeptAbnormalStats(Long deptId, String month);
}
