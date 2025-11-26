package com.ruoyi.common.utils;

import java.math.BigDecimal;

/**
 * 距离计算工具类
 * 
 * @author ruoyi
 */
public class DistanceUtil
{
    // 地球半径
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * 计算两个经纬度之间的距离（单位：米）
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return 距离（米）
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // 保留2位小数
        BigDecimal bg = new BigDecimal(s);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void main(String[] args) {
        // 测试：北京天安门 vs 故宫 (约1km以内)
        System.out.println(getDistance(39.9087, 116.3975, 39.9163, 116.3971));
    }
}
