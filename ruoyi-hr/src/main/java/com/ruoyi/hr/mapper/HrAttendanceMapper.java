package com.ruoyi.hr.mapper;

import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.domain.vo.HrAttendanceReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 考勤打卡 Mapper 接口
 */
@Mapper
public interface HrAttendanceMapper {
    
    /**
     * 插入打卡记录
     */
    int insertHrAttendanceRecord(HrAttendanceRecord record);

    /**
     * 统计个人月报
     * @param userId 用户ID
     * @param month 月份 (yyyy-MM)
     * @return Map containing counts
     */
    Map<String, Object> selectPersonalMonthlyStats(@Param("userId") Long userId, @Param("month") String month);

    /**
     * 统计科室异常人数
     * @param deptId 科室ID
     * @param month 月份
     * @return 异常人数 count
     */
    int selectDeptAbnormalCount(@Param("deptId") Long deptId, @Param("month") String month);

    /**
     * 统计科室总排班人天 (排除休息)
     */
    int selectDeptScheduledCount(@Param("deptId") Long deptId, @Param("month") String month);

    /**
     * 统计科室实际正常出勤人天
     */
    int selectDeptActualNormalCount(@Param("deptId") Long deptId, @Param("month") String month);

    /**
     * 查询考勤月报列表
     * @param deptId 部门ID
     * @param month 月份 (yyyy-MM)
     * @return 月报列表
     */
    List<HrAttendanceReportVO> selectAttendanceReport(@Param("deptId") Long deptId, @Param("month") String month);
}
