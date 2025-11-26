package com.ruoyi.hr.service.impl;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.mapper.HrAttendanceRecordMapper;
import com.ruoyi.hr.service.IHrAttendanceService;

/**
 * 考勤服务实现
 */
@Service
public class HrAttendanceServiceImpl implements IHrAttendanceService {

    @Autowired
    private HrAttendanceRecordMapper recordMapper;

    @Override
    public AjaxResult insertAttendanceRecord(HrAttendanceRecord record) {
        if (record.getCheckTime() == null) {
            record.setCheckTime(new Date());
        }
        
        int rows = recordMapper.insert(record);
        
        if (rows > 0) {
            return AjaxResult.success("打卡成功", record);
        }
        return AjaxResult.error("打卡失败，请联系管理员");
    }
}
