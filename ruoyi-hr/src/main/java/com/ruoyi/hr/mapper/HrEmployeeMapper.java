package com.ruoyi.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.hr.domain.HrEmployee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 员工档案Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
public interface HrEmployeeMapper extends BaseMapper<HrEmployee>
{
    /**
     * 查询员工列表（关联SysUser）
     */
    @Select("SELECT e.*, u.user_name, u.nick_name, u.dept_id, u.phonenumber, d.dept_name " +
            "FROM hr_employee_info e " +
            "LEFT JOIN sys_user u ON e.user_id = u.user_id " +
            "LEFT JOIN sys_dept d ON u.dept_id = d.dept_id " +
            "WHERE u.del_flag = '0' " +
            "${ew.customSqlSegment}")
    List<HrEmployee> selectEmployeeList(@Param("ew") com.baomidou.mybatisplus.core.conditions.Wrapper<HrEmployee> wrapper);
}

