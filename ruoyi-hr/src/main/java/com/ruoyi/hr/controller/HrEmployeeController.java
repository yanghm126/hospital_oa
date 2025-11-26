package com.ruoyi.hr.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hr.domain.HrEmployee;
import com.ruoyi.hr.service.IHrEmployeeService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工档案Controller
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/hr/employee")
public class HrEmployeeController extends BaseController
{
    @Autowired
    private IHrEmployeeService hrEmployeeService;

    /**
     * 查询员工档案列表
     */
    @PreAuthorize("@ss.hasPermi('hr:employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrEmployee hrEmployee)
    {
        startPage();
        List<HrEmployee> list = hrEmployeeService.selectEmployeeList(hrEmployee);
        return getDataTable(list);
    }

    /**
     * 获取员工档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:employee:query')")
    @GetMapping(value = "/{employeeId}")
    public AjaxResult getInfo(@PathVariable("employeeId") Long employeeId)
    {
        return AjaxResult.success(hrEmployeeService.getById(employeeId));
    }

    /**
     * 新增员工档案
     */
    @PreAuthorize("@ss.hasPermi('hr:employee:add')")
    @Log(title = "员工档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrEmployee hrEmployee)
    {
        hrEmployee.setCreateBy(getUsername());
        return toAjax(hrEmployeeService.insertEmployee(hrEmployee));
    }

    /**
     * 修改员工档案
     */
    @PreAuthorize("@ss.hasPermi('hr:employee:edit')")
    @Log(title = "员工档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmployee hrEmployee)
    {
        hrEmployee.setUpdateBy(getUsername());
        return toAjax(hrEmployeeService.updateEmployee(hrEmployee));
    }

    /**
     * 删除员工档案
     */
    @PreAuthorize("@ss.hasPermi('hr:employee:remove')")
    @Log(title = "员工档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{employeeIds}")
    public AjaxResult remove(@PathVariable Long[] employeeIds)
    {
        // TODO: 级联删除逻辑
        return toAjax(hrEmployeeService.removeBatchByIds(java.util.Arrays.asList(employeeIds)));
    }
}

