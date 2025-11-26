package com.ruoyi.hr.domain;

import java.util.Map;

/**
 * 排班矩阵DTO
 */
public class ScheduleMatrixDTO {
    private Long userId;
    private String userName;
    private String deptName;
    // Key: 日期 (如 "01", "02", "31"), Value: shiftId
    private Map<String, Long> scheduleMap;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public Map<String, Long> getScheduleMap() { return scheduleMap; }
    public void setScheduleMap(Map<String, Long> scheduleMap) { this.scheduleMap = scheduleMap; }
}

