package com.ruoyi.hr.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.service.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考勤报表 Controller
 */
@RestController
@RequestMapping("/hr/report")
public class HrReportController extends BaseController
{
    @Autowired
    private IAttendanceService attendanceService;

    /**
     * 获取个人月报数据
     * @param month 月份 (yyyy-MM)
     * @param userId 用户ID (可选，默认当前用户)
     */
    @PreAuthorize("@ss.hasPermi('hr:report:query')")
    @GetMapping("/monthly/personal")
    public AjaxResult getPersonalMonthlyStats(@RequestParam("month") String month, 
                                              @RequestParam(value = "userId", required = false) Long userId)
    {
        if (userId == null) {
            userId = getUserId();
        }
        return AjaxResult.success(attendanceService.getPersonalMonthlyStats(userId, month));
    }

    /**
     * 获取科室统计数据
     * @param deptId 部门ID
     * @param month 月份 (yyyy-MM)
     */
    @PreAuthorize("@ss.hasPermi('hr:report:query')")
    @GetMapping("/monthly/dept")
    public AjaxResult getDeptMonthlyStats(@RequestParam("deptId") Long deptId, 
                                          @RequestParam("month") String month)
    {
        return AjaxResult.success(attendanceService.getDeptAbnormalStats(deptId, month));
    }
}

