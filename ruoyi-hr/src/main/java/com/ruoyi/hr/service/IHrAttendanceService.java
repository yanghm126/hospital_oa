package com.ruoyi.hr.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrAttendanceRecord;

public interface IHrAttendanceService {
    /**
     * 插入打卡记录
     */
    AjaxResult insertAttendanceRecord(HrAttendanceRecord record);
}

