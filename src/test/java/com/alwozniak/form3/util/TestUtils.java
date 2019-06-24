package com.alwozniak.form3.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Helper methods used in tests.
 */
public class TestUtils {

    public static Date getDateOf(int dayOfMonth, int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }
}
