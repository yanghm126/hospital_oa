package com.ruoyi.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.hr.domain.HrAuditApply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 审批申请Mapper接口
 */
public interface HrAuditApplyMapper extends BaseMapper<HrAuditApply>
{
    @Select("SELECT a.*, u.nick_name as user_name, d.dept_name " +
            "FROM hr_audit_apply a " +
            "LEFT JOIN sys_user u ON a.user_id = u.user_id " +
            "LEFT JOIN sys_dept d ON a.dept_id = d.dept_id " +
            "${ew.customSqlSegment}")
    List<HrAuditApply> selectApplyList(@Param("ew") com.baomidou.mybatisplus.core.conditions.Wrapper<HrAuditApply> wrapper);
}

