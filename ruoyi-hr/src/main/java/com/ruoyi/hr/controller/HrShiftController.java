package com.ruoyi.hr.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.hr.domain.HrShift;
import com.ruoyi.hr.service.IHrShiftService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 班次Controller
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/hr/shift")
public class HrShiftController extends BaseController
{
    @Autowired
    private IHrShiftService hrShiftService;

    /**
     * 查询班次列表
     */
    @PreAuthorize("@ss.hasPermi('hr:shift:list')")
    @GetMapping("/list")
    public TableDataInfo list(HrShift hrShift)
    {
        startPage();
        List<HrShift> list = hrShiftService.selectHrShiftList(hrShift);
        return getDataTable(list);
    }

    /**
     * 导出班次列表
     */
    @PreAuthorize("@ss.hasPermi('hr:shift:export')")
    @Log(title = "班次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrShift hrShift)
    {
        List<HrShift> list = hrShiftService.selectHrShiftList(hrShift);
        ExcelUtil<HrShift> util = new ExcelUtil<HrShift>(HrShift.class);
        util.exportExcel(response, list, "班次数据");
    }

    /**
     * 获取班次详细信息
     */
    @PreAuthorize("@ss.hasPermi('hr:shift:query')")
    @GetMapping(value = "/{shiftId}")
    public AjaxResult getInfo(@PathVariable("shiftId") Long shiftId)
    {
        return AjaxResult.success(hrShiftService.getById(shiftId));
    }

    /**
     * 新增班次
     */
    @PreAuthorize("@ss.hasPermi('hr:shift:add')")
    @Log(title = "班次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrShift hrShift)
    {
        hrShift.setCreateBy(getUsername());
        return toAjax(hrShiftService.insertHrShift(hrShift));
    }

    /**
     * 修改班次
     */
    @PreAuthorize("@ss.hasPermi('hr:shift:edit')")
    @Log(title = "班次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrShift hrShift)
    {
        hrShift.setUpdateBy(getUsername());
        return toAjax(hrShiftService.updateHrShift(hrShift));
    }

    /**
     * 删除班次
     */
    @PreAuthorize("@ss.hasPermi('hr:shift:remove')")
    @Log(title = "班次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{shiftIds}")
    public AjaxResult remove(@PathVariable Long[] shiftIds)
    {
        return toAjax(hrShiftService.deleteHrShiftByIds(shiftIds));
    }
}
