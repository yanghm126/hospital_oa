package com.ruoyi.hr.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.service.IHrAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 考勤打卡 Controller
 */
@RestController
@RequestMapping("/hr/attendance")
public class HrAttendanceController extends BaseController {

    @Autowired
    private IHrAttendanceService attendanceService;

    /**
     * 移动端打卡
     */
    @PostMapping("/clockin")
    public AjaxResult clockIn(@RequestBody HrAttendanceRecord record) {
        record.setUserId(SecurityUtils.getUserId());
        return attendanceService.insertAttendanceRecord(record);
    }
}

