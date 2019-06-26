package com.alwozniak.form3.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Helper methods used in tests.
 */
public class TestUtils {

    public static final String TEST_RESOURCES_PATH = "src/test/resources/test-files/";

    public static Date getDateOf(int dayOfMonth, int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    public static byte[] getTestResource(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(TEST_RESOURCES_PATH + fileName));
    }
}
