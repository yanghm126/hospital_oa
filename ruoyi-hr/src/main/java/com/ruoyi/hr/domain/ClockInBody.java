package com.ruoyi.hr.domain;

import java.math.BigDecimal;

/**
 * 打卡参数 Body
 */
public class ClockInBody {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private String remark;

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

