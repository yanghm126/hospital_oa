package com.ruoyi.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 班次对象 hr_shift
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("hr_shift")
public class HrShift extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 班次ID */
    @TableId(type = IdType.AUTO)
    private Long shiftId;

    /** 班次名称 */
    @Excel(name = "班次名称")
    private String shiftName;

    /** 上班时间 */
    @Excel(name = "上班时间")
    private String startTime;

    /** 下班时间 */
    @Excel(name = "下班时间")
    private String endTime;

    /** 是否跨天 (0否 1是) */
    @Excel(name = "是否跨天", readConverterExp = "0=否,1=是")
    private Integer crossDay;

    /** 迟到容忍时长(分钟) */
    @Excel(name = "迟到容忍时长(分钟)")
    private Integer lateMinutes;

    /** 早退容忍时长(分钟) */
    @Excel(name = "早退容忍时长(分钟)")
    private Integer earlyLeaveMinutes;
    
    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
