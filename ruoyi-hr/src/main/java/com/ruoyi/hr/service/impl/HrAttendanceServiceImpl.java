package com.ruoyi.hr.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DistanceUtil;
import com.ruoyi.hr.domain.HrAttendanceGroup;
import com.ruoyi.hr.domain.HrAttendanceRecord;
import com.ruoyi.hr.service.IHrAttendanceService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class HrAttendanceServiceImpl implements IHrAttendanceService {

    // @Autowired private HrAttendanceRecordMapper recordMapper;
    // @Autowired private HrAttendanceGroupMapper groupMapper;

    @Override
    public AjaxResult insertAttendanceRecord(HrAttendanceRecord record) {
        // 1. 获取当前用户所属考勤组 (模拟数据)
        HrAttendanceGroup group = getMockGroup(record.getUserId());
        if (group == null) {
            return AjaxResult.error("未找到考勤组");
        }

        // 2. 计算距离
        double distance = DistanceUtil.getDistance(
            record.getLatitude().doubleValue(), record.getLongitude().doubleValue(),
            group.getLatitude().doubleValue(), group.getLongitude().doubleValue()
        );
        record.setDistance((long) distance);

        // 3. 判断是否在范围内
        if (distance <= group.getAllowedRadius()) {
            record.setResult("1"); // 正常
        } else {
            record.setResult("4"); // 外勤
        }
        
        record.setCheckTime(new Date());
        
        // 4. 插入数据库 (需生成Mapper)
        // recordMapper.insertHrAttendanceRecord(record);
        
        System.out.println("打卡成功: " + record.getResult() + ", 距离: " + distance);
        
        return AjaxResult.success("打卡成功", record);
    }

    private HrAttendanceGroup getMockGroup(Long userId) {
        HrAttendanceGroup group = new HrAttendanceGroup();
        group.setAllowedRadius(500);
        // 模拟一个中心点 (例如北京天安门)
        group.setLatitude(new BigDecimal("39.908823")); 
        group.setLongitude(new BigDecimal("116.397470")); 
        return group;
    }
}

