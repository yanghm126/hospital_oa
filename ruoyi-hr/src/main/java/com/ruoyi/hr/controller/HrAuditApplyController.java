package com.ruoyi.hr.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.hr.domain.HrAuditApply;
import com.ruoyi.hr.service.IHrAuditApplyService;

/**
 * 审批 Controller
 */
@RestController
@RequestMapping("/hr/audit")
public class HrAuditApplyController extends BaseController
{
    @Autowired
    private IHrAuditApplyService auditService;

    /**
     * 查询我的申请列表
     */
    @GetMapping("/my")
    public TableDataInfo listMy(HrAuditApply apply)
    {
        startPage();
        apply.setUserId(getUserId());
        List<HrAuditApply> list = auditService.selectApplyList(apply);
        return getDataTable(list);
    }
    
    /**
     * 查询待我审批列表
     */
    @GetMapping("/todo")
    public TableDataInfo listTodo(HrAuditApply apply)
    {
        startPage();
        apply.setAuditorId(getUserId());
        apply.setStatus("0"); // 只查待审
        List<HrAuditApply> list = auditService.selectApplyList(apply);
        return getDataTable(list);
    }

    /**
     * 提交申请
     */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody HrAuditApply apply)
    {
        return toAjax(auditService.submitApply(apply));
    }

    /**
     * 审批操作
     */
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody HrAuditApply apply)
    {
        return toAjax(auditService.auditApply(apply.getApplyId(), apply.getStatus(), apply.getAuditMsg()));
    }
}

