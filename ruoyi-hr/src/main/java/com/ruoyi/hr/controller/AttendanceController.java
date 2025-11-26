package com.ruoyi.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.hr.domain.ClockInBody;
import com.ruoyi.hr.service.IAttendanceService;

/**
 * 考勤打卡 Controller
 */
@RestController
@RequestMapping("/app/hr/attendance")
public class AttendanceController extends BaseController
{
    @Autowired
    private IAttendanceService attendanceService;

    /**
     * 移动端打卡
     */
    @PostMapping("/clock")
    public AjaxResult clock(@RequestBody ClockInBody body)
    {
        LoginUser loginUser = getLoginUser();
        return attendanceService.clockIn(loginUser.getUserId(), loginUser.getDeptId(), body);
    }
}

