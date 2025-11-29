package com.ruoyi.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void differentDaysByMillisecondUsesAbsoluteDifference() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        Date earlier = calendar.getTime();

        calendar.set(2024, Calendar.JANUARY, 10, 0, 0, 0);
        Date later = calendar.getTime();

        assertEquals(9L, DateUtils.differentDaysByMillisecond(later, earlier));
        assertEquals(9L, DateUtils.differentDaysByMillisecond(earlier, later));
    }

    @Test
    void timeDistanceRejectsNullInputs() {
        Date now = new Date();
        assertThrows(IllegalArgumentException.class, () -> DateUtils.timeDistance(null, now));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.timeDistance(now, null));
    }
}
