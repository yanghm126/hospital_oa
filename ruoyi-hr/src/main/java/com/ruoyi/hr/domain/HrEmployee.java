package com.ruoyi.hr.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 员工档案对象 hr_employee_info
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("hr_employee_info")
public class HrEmployee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工ID */
    @TableId(type = IdType.AUTO)
    private Long employeeId;

    /** 系统用户ID */
    private Long userId;

    /** 工号 */
    @Excel(name = "工号")
    private String empNo;

    /** 员工状态（0在编 1退休 2离职） */
    @Excel(name = "员工状态", readConverterExp = "0=在编,1=退休,2=离职")
    private String status;

    /** 编制类型 */
    @Excel(name = "编制类型")
    private String empType;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireDate;

    /** 紧急联系人 */
    @Excel(name = "紧急联系人")
    private String emergencyContact;

    /** 关联的系统用户对象 */
    @TableField(exist = false)
    private SysUser sysUser;
}
