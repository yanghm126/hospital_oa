package com.ruoyi.hr.controller;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.service.IHrEmployeeService;

/**
 * 移动端考勤Controller
 */
@RestController
@RequestMapping("/hr/attendance/mobile")
public class MobileAttendanceController extends BaseController {
    
    // 模拟保存，实际应注入 Service
    // @Autowired
    // private IHrAttendanceRecordService recordService;

    /**
     * 打卡接口
     */
    @PostMapping("/checkin")
    public AjaxResult checkIn(@RequestBody CheckInBody body) {
        // 1. 获取当前用户
        Long userId = getUserId();
        
        // 2. 模拟获取考勤组规则 (实际应查询 HrAttendanceGroup)
        // 假设考勤点在 (30.000, 120.000)，半径 200米
        double groupLat = 30.000;
        double groupLng = 120.000;
        int radius = 500000; // 暂时放宽方便测试

        // 3. 计算距离
        double distance = getDistance(body.getLatitude().doubleValue(), body.getLongitude().doubleValue(), groupLat, groupLng);
        
        if (distance > radius) {
            return AjaxResult.error("不在考勤范围内，当前距离：" + (int)distance + "米");
        }

        // 4. 保存打卡记录
        HrAttendanceRecord record = new HrAttendanceRecord();
        record.setUserId(userId);
        record.setCheckTime(new Date());
        record.setLatitude(body.getLatitude());
        record.setLongitude(body.getLongitude());
        record.setAddress(body.getAddress());
        record.setDistance((long)distance);
        record.setResult("1"); // 正常
        record.setCheckType("1"); // 上班

        // recordService.save(record);
        
        return AjaxResult.success("打卡成功", record);
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        // 简单估算或 Haversine
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;
        return s;
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

