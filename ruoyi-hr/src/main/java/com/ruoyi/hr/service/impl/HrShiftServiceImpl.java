package com.ruoyi.hr.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.hr.mapper.HrShiftMapper;
import com.ruoyi.hr.domain.HrShift;
import com.ruoyi.hr.service.IHrShiftService;

/**
 * 班次Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-26
 */
@Service
public class HrShiftServiceImpl extends ServiceImpl<HrShiftMapper, HrShift> implements IHrShiftService
{
    @Autowired
    private HrShiftMapper hrShiftMapper;

    /**
     * 查询班次
     * 
     * @param shiftId 班次ID
     * @return 班次
     */
    @Override
    public HrShift selectHrShiftById(Long shiftId)
    {
        return hrShiftMapper.selectHrShiftById(shiftId);
    }

    /**
     * 查询班次列表
     * 
     * @param hrShift 班次
     * @return 班次
     */
    @Override
    public List<HrShift> selectHrShiftList(HrShift hrShift)
    {
        return hrShiftMapper.selectHrShiftList(hrShift);
    }

    /**
     * 新增班次
     * 
     * @param hrShift 班次
     * @return 结果
     */
    @Override
    public int insertHrShift(HrShift hrShift)
    {
        hrShift.setCreateTime(DateUtils.getNowDate());
        return hrShiftMapper.insertHrShift(hrShift);
    }

    /**
     * 修改班次
     * 
     * @param hrShift 班次
     * @return 结果
     */
    @Override
    public int updateHrShift(HrShift hrShift)
    {
        hrShift.setUpdateTime(DateUtils.getNowDate());
        return hrShiftMapper.updateHrShift(hrShift);
    }

    /**
     * 批量删除班次
     * 
     * @param shiftIds 需要删除的班次ID
     * @return 结果
     */
    @Override
    public int deleteHrShiftByIds(Long[] shiftIds)
    {
        return hrShiftMapper.deleteHrShiftByIds(shiftIds);
    }

    /**
     * 删除班次信息
     * 
     * @param shiftId 班次ID
     * @return 结果
     */
    @Override
    public int deleteHrShiftById(Long shiftId)
    {
        return hrShiftMapper.deleteById(shiftId);
    }
}
