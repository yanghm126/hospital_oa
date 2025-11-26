package com.ruoyi.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.hr.mapper.HrEmployeeMapper;
import com.ruoyi.hr.domain.HrEmployee;
import com.ruoyi.hr.service.IHrEmployeeService;

/**
 * 员工档案Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Service
public class HrEmployeeServiceImpl extends ServiceImpl<HrEmployeeMapper, HrEmployee> implements IHrEmployeeService
{
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private HrEmployeeMapper hrEmployeeMapper;

    /**
     * 查询员工档案列表
     */
    @Override
    public List<HrEmployee> selectEmployeeList(HrEmployee hrEmployee)
    {
        // 这里简单演示，实际上Mapper中需要写关联查询的XML或注解
        // 为了方便，我们暂时只查询HrEmployee，具体关联可以在Controller组装或使用MP的关联查询
        QueryWrapper<HrEmployee> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(hrEmployee.getEmpNo())) {
            queryWrapper.eq("emp_no", hrEmployee.getEmpNo());
        }
        // TODO: 完善Mapper中的关联查询
        return hrEmployeeMapper.selectEmployeeList(queryWrapper);
    }

    /**
     * 新增员工
     */
    @Override
    @Transactional
    public boolean insertEmployee(HrEmployee hrEmployee)
    {
        SysUser user = hrEmployee.getSysUser();
        if (user != null) {
            // 1. 插入系统用户
            if (StringUtils.isEmpty(user.getUserName())) {
                user.setUserName(hrEmployee.getEmpNo()); // 默认工号为账号
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                user.setPassword("123456"); // 默认密码
            }
            user.setCreateBy(hrEmployee.getCreateBy());
            sysUserService.insertUser(user);
            
            // 2. 关联用户ID
            hrEmployee.setUserId(user.getUserId());
        }
        
        hrEmployee.setCreateTime(DateUtils.getNowDate());
        return save(hrEmployee);
    }

    /**
     * 修改员工
     */
    @Override
    @Transactional
    public boolean updateEmployee(HrEmployee hrEmployee)
    {
        SysUser user = hrEmployee.getSysUser();
        if (user != null && user.getUserId() != null) {
            sysUserService.updateUser(user);
        }
        hrEmployee.setUpdateTime(DateUtils.getNowDate());
        return updateById(hrEmployee);
    }
}

