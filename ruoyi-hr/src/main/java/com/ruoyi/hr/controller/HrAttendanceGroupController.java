package com.ruoyi.hr.controller;

import java.util.Arrays;
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
import com.ruoyi.hr.domain.HrAttendanceGroup;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 考勤组Controller
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/hr/attendanceGroup")
public class HrAttendanceGroupController extends BaseController
{
    @Autowired
    private IHrAttendanceGroupService attendanceGroupService;

    /**
     * 查询考勤组列表
     */
    @PreAuthorize("@ss.hasPermi('hr:attendanceGroup:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrAttendanceGroup attendanceGroup)
    {
        startPage();
        LambdaQueryWrapper<HrAttendanceGroup> queryWrapper = new LambdaQueryWrapper<>();
        if (attendanceGroup.getGroupName() != null) {
            queryWrapper.like(HrAttendanceGroup::getGroupName, attendanceGroup.getGroupName());
        }
        List<HrAttendanceGroup> list = attendanceGroupService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 导出考勤组列表
     */
    @PreAuthorize("@ss.hasPermi('hr:attendanceGroup:export')")
    @Log(title = "考勤组", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HrAttendanceGroup attendanceGroup)
    {
        LambdaQueryWrapper<HrAttendanceGroup> queryWrapper = new LambdaQueryWrapper<>();
        if (attendanceGroup.getGroupName() != null) {
            queryWrapper.like(HrAttendanceGroup::getGroupName, attendanceGroup.getGroupName());
        }
        List<HrAttendanceGroup> list = attendanceGroupService.list(queryWrapper);
        ExcelUtil<HrAttendanceGroup> util = new ExcelUtil<HrAttendanceGroup>(HrAttendanceGroup.class);
        return util.exportExcel(list, "考勤组数据");
    }

    /**
     * 获取考勤组详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:attendanceGroup:query')")
    @GetMapping(value = "/{groupId}")
    public AjaxResult getInfo(@PathVariable("groupId") Long groupId)
    {
        return AjaxResult.success(attendanceGroupService.getById(groupId));
    }

    /**
     * 新增考勤组
     */
    @PreAuthorize("@ss.hasPermi('hr:attendanceGroup:add')")
    @Log(title = "考勤组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrAttendanceGroup attendanceGroup)
    {
        return toAjax(attendanceGroupService.save(attendanceGroup));
    }

    /**
     * 修改考勤组
     */
    @PreAuthorize("@ss.hasPermi('hr:attendanceGroup:edit')")
    @Log(title = "考勤组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrAttendanceGroup attendanceGroup)
    {
        return toAjax(attendanceGroupService.updateById(attendanceGroup));
    }

    /**
     * 删除考勤组
     */
    @PreAuthorize("@ss.hasPermi('hr:attendanceGroup:remove')")
    @Log(title = "考勤组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{groupIds}")
    public AjaxResult remove(@PathVariable Long[] groupIds)
    {
        return toAjax(attendanceGroupService.removeByIds(Arrays.asList(groupIds)));
    }
}

