package com.ruoyi.hr.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DistanceUtil;
import com.ruoyi.hr.domain.*;
import com.ruoyi.hr.mapper.*;
import com.ruoyi.hr.service.IAttendanceService;

@Service
public class AttendanceServiceImpl implements IAttendanceService
{
    @Autowired
    private HrAttendanceGroupMapper groupMapper;

    @Autowired
    private HrAttendanceRecordMapper recordMapper;

    @Autowired
    private HrAttendanceMapper attendanceMapper; // 注入统计专用Mapper

    @Autowired
    private HrScheduleMapper scheduleMapper;
    
    @Autowired
    private HrShiftMapper shiftMapper;

    @Override
    @Transactional
    public AjaxResult clockIn(Long userId, Long deptId, ClockInBody body)
    {
        // 1. 查询考勤组
        HrAttendanceGroup group = groupMapper.selectByDeptId(deptId);
        if (group == null) {
            return AjaxResult.error("未找到您的考勤组规则，请联系管理员");
        }

        // 2. 计算距离
        double distance = DistanceUtil.getDistance(
            body.getLatitude().doubleValue(), 
            body.getLongitude().doubleValue(), 
            group.getLatitude().doubleValue(), 
            group.getLongitude().doubleValue()
        );

        // 3. 状态判定
        String resultStatus = "1"; // 默认正常
        if (distance > group.getAllowedRadius()) {
            resultStatus = "4"; // 外勤
        }

        // 4. 班次与时间判定
        String todayStr = DateUtils.parseDateToStr("yyyy-MM-dd", new Date());
        HrSchedule schedule = scheduleMapper.selectByUserAndDate(userId, todayStr);
        
        String checkType = "1"; // 默认上班
        // 简单逻辑：上午打卡算上班，下午打卡算下班 (实际应根据班次时间动态判断)
        Date now = new Date();
        int hour = Integer.parseInt(DateUtils.parseDateToStr("HH", now));
        if (hour > 12) {
            checkType = "2"; // 下班
        }

        if (schedule != null && schedule.getShiftId() != null) {
             HrShift shift = shiftMapper.selectById(schedule.getShiftId());
             if (shift != null) {
                 // TODO: 这里应解析 shift.startTime (HH:mm:ss) 与当前时间对比
             }
        }

        // 5. 保存记录
        HrAttendanceRecord record = new HrAttendanceRecord();
        record.setUserId(userId);
        record.setCheckTime(now);
        record.setCheckType(checkType);
        record.setResult(resultStatus);
        record.setLatitude(body.getLatitude());
        record.setLongitude(body.getLongitude());
        record.setAddress(body.getAddress());
        record.setDistance((long)distance);
        record.setRemark(body.getRemark());
        
        recordMapper.insert(record);

        if ("4".equals(resultStatus)) {
            return AjaxResult.success("打卡成功(外勤)，距离考勤点" + (int)distance + "米");
        }
        return AjaxResult.success("打卡成功");
    }

    @Override
    public Map<String, Object> getPersonalMonthlyStats(Long userId, String month) {
        return attendanceMapper.selectPersonalMonthlyStats(userId, month);
    }

    @Override
    public Map<String, Object> getDeptAbnormalStats(Long deptId, String month) {
        Map<String, Object> result = new HashMap<>();
        // 异常人数
        int abnormalCount = attendanceMapper.selectDeptAbnormalCount(deptId, month);
        result.put("abnormalCount", abnormalCount);
        
        // 可以扩展更多数据，例如：
        // int totalScheduled = attendanceMapper.selectDeptScheduledCount(deptId, month);
        // result.put("totalScheduled", totalScheduled);
        
        return result;
    }
}
