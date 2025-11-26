package com.ruoyi.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.hr.domain.HrAttendanceGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 考勤组 Mapper
 */
@Mapper
public interface HrAttendanceGroupMapper extends BaseMapper<HrAttendanceGroup>
{
    /**
     * 查询考勤组
     * 
     * @param groupId 考勤组ID
     * @return 考勤组
     */
    public HrAttendanceGroup selectHrAttendanceGroupById(Long groupId);

    /**
     * 查询考勤组列表
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 考勤组集合
     */
    public List<HrAttendanceGroup> selectHrAttendanceGroupList(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 新增考勤组
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 结果
     */
    public int insertHrAttendanceGroup(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 修改考勤组
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 结果
     */
    public int updateHrAttendanceGroup(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 删除考勤组
     * 
     * @param groupId 考勤组ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupById(Long groupId);

    /**
     * 批量删除考勤组
     * 
     * @param groupIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupByIds(Long[] groupIds);

    /**
     * 根据部门ID查询考勤组
     */
    HrAttendanceGroup selectByDeptId(@Param("deptId") Long deptId);
}
