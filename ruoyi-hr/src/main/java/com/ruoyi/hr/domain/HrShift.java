package com.ruoyi.hr.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    /** 是否跨天 */
    @Excel(name = "是否跨天", readConverterExp = "0=否,1=是")
    private Integer crossDay;
}
