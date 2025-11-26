package com.ruoyi.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.hr.domain.HrEmployee;

/**
 * 员工档案Service接口
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
public interface IHrEmployeeService extends IService<HrEmployee>
{
    /**
     * 查询员工档案列表
     */
    List<HrEmployee> selectEmployeeList(HrEmployee hrEmployee);

    /**
     * 新增员工（包含账号）
     */
    boolean insertEmployee(HrEmployee hrEmployee);

    /**
     * 修改员工
     */
    boolean updateEmployee(HrEmployee hrEmployee);
}

