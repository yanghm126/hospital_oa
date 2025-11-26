package com.ruoyi.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.hr.domain.HrAuditApply;

/**
 * 审批申请Service接口
 */
public interface IHrAuditApplyService extends IService<HrAuditApply>
{
    List<HrAuditApply> selectApplyList(HrAuditApply hrAuditApply);
    
    /**
     * 提交申请
     */
    boolean submitApply(HrAuditApply apply);
    
    /**
     * 审批
     * @param applyId 申请ID
     * @param status 状态(1通过 2驳回)
     * @param msg 意见
     */
    boolean auditApply(Long applyId, String status, String msg);
}

