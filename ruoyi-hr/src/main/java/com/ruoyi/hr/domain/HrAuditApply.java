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
 * 审批申请对象 hr_audit_apply
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("hr_audit_apply")
public class HrAuditApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    @TableId(type = IdType.AUTO)
    private Long applyId;

    /** 申请人ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 申请类型（1请假 2补卡） */
    @Excel(name = "申请类型", readConverterExp = "1=请假,2=补卡")
    private String applyType;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 事由 */
    @Excel(name = "事由")
    private String reason;

    /** 当前审核人ID */
    private Long auditorId;
    
    /** 审核人姓名 (冗余或关联) */
    private transient String auditorName;

    /** 状态（0待审 1通过 2驳回） */
    @Excel(name = "状态", readConverterExp = "0=待审,1=通过,2=驳回")
    private String status;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /** 审核意见 */
    private String auditMsg;
    
    /** 申请人姓名 */
    private transient String userName;
}
