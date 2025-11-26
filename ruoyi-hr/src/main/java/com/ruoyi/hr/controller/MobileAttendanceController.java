package com.ruoyi.hr.controller;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DistanceUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.hr.domain.HrAttendanceGroup;
import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.domain.HrSchedule;
import com.ruoyi.hr.domain.HrShift;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import com.ruoyi.hr.service.IHrAttendanceService;
import com.ruoyi.hr.service.IHrScheduleService;
import com.ruoyi.hr.service.IHrShiftService;

/**
 * 移动端考勤Controller
 */
@RestController
@RequestMapping("/hr/attendance/mobile")
public class MobileAttendanceController extends BaseController {
    
    @Autowired
    private IHrAttendanceService attendanceService;

    @Autowired
    private IHrAttendanceGroupService attendanceGroupService;

    @Autowired
    private IHrScheduleService scheduleService;

    @Autowired
    private IHrShiftService shiftService;

    /**
     * 打卡接口
     */
    @PostMapping("/checkin")
    public AjaxResult checkIn(@RequestBody CheckInBody body) {
        // 1. 获取当前用户和部门
        Long userId = getUserId();
        Long deptId = SecurityUtils.getDeptId();
        
        // 2. 查询考勤组规则
        HrAttendanceGroup group = attendanceGroupService.selectAttendanceGroupByDeptId(deptId);
        if (group == null) {
            return AjaxResult.error("未找到您的考勤组规则，请联系管理员");
        }

        // 3. 计算距离
        double distance = DistanceUtil.getDistance(
            body.getLatitude().doubleValue(), 
            body.getLongitude().doubleValue(), 
            group.getLatitude().doubleValue(), 
            group.getLongitude().doubleValue()
        );
        
        if (distance > group.getAllowedRadius()) {
            return AjaxResult.error("不在考勤范围内，当前距离：" + (int)distance + "米");
        }

        // 4. 获取班次信息
        // 获取当天日期 (yyyy-MM-dd 00:00:00)
        Date workDate = DateUtils.parseDate(DateUtils.getDate());
        
        LambdaQueryWrapper<HrSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HrSchedule::getUserId, userId)
                    .eq(HrSchedule::getWorkDate, workDate);
        
        HrSchedule schedule = scheduleService.getOne(queryWrapper);
        
        String checkType = "1"; // 默认上班
        String result = "1"; // 默认正常
        
        Date now = new Date();
        String nowTimeStr = DateUtils.parseDateToStr("HH:mm", now);
        int hour = Integer.parseInt(DateUtils.parseDateToStr("HH", now));
        
        // 简单判断：12点前算上班，12点后算下班
        if (hour >= 12) {
            checkType = "2"; // 下班
        }

        if (schedule != null && schedule.getShiftId() != null) {
            HrShift shift = shiftService.getById(schedule.getShiftId());
            if (shift != null) {
                if ("1".equals(checkType)) {
                    // 上班打卡：如果当前时间 > 上班时间，记为迟到
                    if (nowTimeStr.compareTo(shift.getStartTime()) > 0) {
                        result = "2"; // 迟到
                    }
                } else {
                    // 下班打卡：如果当前时间 < 下班时间，记为早退
                    if (nowTimeStr.compareTo(shift.getEndTime()) < 0) {
                        result = "3"; // 早退
                    }
                }
            }
        }

        // 5. 保存打卡记录
        HrAttendanceRecord record = new HrAttendanceRecord();
        record.setUserId(userId);
        record.setCheckTime(now);
        record.setLatitude(body.getLatitude());
        record.setLongitude(body.getLongitude());
        record.setAddress(body.getAddress());
        record.setDistance((long)distance);
        record.setResult(result);
        record.setCheckType(checkType);

        return attendanceService.insertAttendanceRecord(record);
    }

    public static class CheckInBody {
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String address;

        public BigDecimal getLatitude() { return latitude; }
        public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
        public BigDecimal getLongitude() { return longitude; }
        public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
}
