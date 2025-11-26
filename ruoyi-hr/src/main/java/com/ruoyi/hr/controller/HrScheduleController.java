package com.ruoyi.hr.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hr.domain.HrSchedule;
import com.ruoyi.hr.domain.ScheduleMatrixDTO;
import com.ruoyi.hr.service.IHrScheduleService;

/**
 * 排班管理Controller
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/hr/schedule")
public class HrScheduleController extends BaseController
{
    @Autowired
    private IHrScheduleService hrScheduleService;

    /**
     * 查询排班矩阵
     */
    @PreAuthorize("@ss.hasPermi('hr:schedule:list')")
    @GetMapping("/matrix")
    public AjaxResult matrix(@RequestParam(required = false) Long deptId, 
                             @RequestParam String month)
    {
        List<ScheduleMatrixDTO> list = hrScheduleService.selectScheduleMatrix(deptId, month);
        return AjaxResult.success(list);
    }

    /**
     * 批量保存排班
     */
    @PreAuthorize("@ss.hasPermi('hr:schedule:edit')")
    @Log(title = "排班管理", businessType = BusinessType.UPDATE)
    @PostMapping("/batchSave")
    public AjaxResult batchSave(@RequestBody List<HrSchedule> scheduleList)
    {
        return toAjax(hrScheduleService.saveScheduleBatch(scheduleList));
    }
}
