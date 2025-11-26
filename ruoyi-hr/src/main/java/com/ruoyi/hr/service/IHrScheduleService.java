package com.ruoyi.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.hr.domain.HrSchedule;
import com.ruoyi.hr.domain.ScheduleMatrixDTO;

/**
 * 排班Service接口
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
public interface IHrScheduleService extends IService<HrSchedule>
{
    /**
     * 查询排班矩阵
     * @param deptId 科室ID
     * @param month 月份 (yyyy-MM)
     */
    List<ScheduleMatrixDTO> selectScheduleMatrix(Long deptId, String month);

    /**
     * 批量保存排班
     * @param scheduleList 排班列表
     */
    boolean saveScheduleBatch(List<HrSchedule> scheduleList);
}

