package com.ruoyi.hr.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 考勤打卡记录对象 hr_attendance_record
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("hr_attendance_record")
public class HrAttendanceRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String month;

    @TableField(exist = false)
    private Long deptId;

    private Long recordId;

    private Long userId;

    @Excel(name = "打卡时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    @Excel(name = "类型", readConverterExp = "1=上班,2=下班")
    private String checkType;

    @Excel(name = "结果", readConverterExp = "1=正常,2=迟到,3=早退,4=外勤,5=缺卡")
    private String result;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private Long distance;

    private String deviceInfo;
}
