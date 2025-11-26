package com.ruoyi.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.hr.domain.HrShift;

/**
 * 班次Service接口
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
public interface IHrShiftService extends IService<HrShift>
{
    /**
     * 查询班次
     * 
     * @param shiftId 班次ID
     * @return 班次
     */
    public HrShift selectHrShiftById(Long shiftId);

    /**
     * 查询班次列表
     * 
     * @param hrShift 班次
     * @return 班次集合
     */
    public List<HrShift> selectHrShiftList(HrShift hrShift);

    /**
     * 新增班次
     * 
     * @param hrShift 班次
     * @return 结果
     */
    public int insertHrShift(HrShift hrShift);

    /**
     * 修改班次
     * 
     * @param hrShift 班次
     * @return 结果
     */
    public int updateHrShift(HrShift hrShift);

    /**
     * 批量删除班次
     * 
     * @param shiftIds 需要删除的班次ID
     * @return 结果
     */
    public int deleteHrShiftByIds(Long[] shiftIds);

    /**
     * 删除班次信息
     * 
     * @param shiftId 班次ID
     * @return 结果
     */
    public int deleteHrShiftById(Long shiftId);
}
