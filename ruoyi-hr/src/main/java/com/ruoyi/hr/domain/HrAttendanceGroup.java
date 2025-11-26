package com.ruoyi.hr.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 考勤组对象 hr_attendance_group
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("hr_attendance_group")
public class HrAttendanceGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考勤组ID */
    @TableId(type = IdType.AUTO)
    private Long groupId;

    /** 考勤组名称 */
    @Excel(name = "考勤组名称")
    private String groupName;

    /** 允许打卡范围(米) */
    @Excel(name = "允许打卡范围")
    private Integer allowedRadius;

    /** 中心点纬度 */
    private BigDecimal latitude;

    /** 中心点经度 */
    private BigDecimal longitude;

    /** 考勤地点名称 */
    @Excel(name = "考勤地点名称")
    private String address;

    /** 工作日 */
    @Excel(name = "工作日")
    private String workDays;

    /** 适用部门ID集合 */
    private String deptIds;
}
