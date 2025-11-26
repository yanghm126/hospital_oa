package com.ruoyi.hr.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 排班对象 hr_schedule
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("hr_schedule")
public class HrSchedule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long scheduleId;

    private Long deptId;

    private Long userId;
    
    private String userName; // 冗余字段，方便前端显示

    @Excel(name = "工作日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workDate;

    private Long shiftId;
    
    private String shiftName; // 快照
}
