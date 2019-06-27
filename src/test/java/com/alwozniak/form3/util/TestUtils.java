package com.alwozniak.form3.util;

import com.alwozniak.form3.domain.ChargeInfoForCurrency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

    public static void assertSenderChargeInfoForCurrency(List<ChargeInfoForCurrency> senderCharges, String currency,
                                                   double expectedAmount) {
        ChargeInfoForCurrency senderChargeInCurrency = getChargeInfoForCurrency(currency, senderCharges);
        assertThat(senderChargeInCurrency.getAmount(), is(expectedAmount));
    }

    private static ChargeInfoForCurrency getChargeInfoForCurrency(String currency,
                                                                  List<ChargeInfoForCurrency> chargeInfos) {
        return chargeInfos.stream()
                .filter(chargeInfoForCurrency -> chargeInfoForCurrency.getCurrency().equals(currency))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Charge info not found for currency " + currency));
    }
}
