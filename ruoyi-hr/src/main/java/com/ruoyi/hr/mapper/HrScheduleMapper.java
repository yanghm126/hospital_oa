package com.ruoyi.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.hr.domain.HrSchedule;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

/**
 * 排班 Mapper
 */
public interface HrScheduleMapper extends BaseMapper<HrSchedule>
{
    @Select("SELECT * FROM hr_schedule WHERE user_id = #{userId} AND work_date = #{workDate} LIMIT 1")
    HrSchedule selectByUserAndDate(@Param("userId") Long userId, @Param("workDate") String workDate);
}

