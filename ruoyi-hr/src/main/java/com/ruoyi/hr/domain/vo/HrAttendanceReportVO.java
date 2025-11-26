package com.ruoyi.hr.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.io.Serializable;

/**
 * 考勤报表 VO
 */
@Data
public class HrAttendanceReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String nickName;

    /** 部门名称 */
    @Excel(name = "部门")
    private String deptName;

    /** 出勤天数 */
    @Excel(name = "出勤天数")
    private Integer attendanceDays;

    /** 迟到次数 */
    @Excel(name = "迟到次数")
    private Integer lateCount;

    /** 早退次数 */
    @Excel(name = "早退次数")
    private Integer earlyLeaveCount;

    /** 缺卡次数 */
    @Excel(name = "缺卡次数")
    private Integer missingCardCount;
}
