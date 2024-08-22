package com.cms.helpdesk.common.utils;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class OtpUtil {
    private static final int DIGIT_OTP = 5;

    public static String generateDigitOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(DIGIT_OTP);

        for (int i = 0; i < DIGIT_OTP; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    public static boolean isMoreThanFiveMinutesOTP(Timestamp updatedDate) {
        LocalDateTime theUpdatedDate = updatedDate.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        long minutes = ChronoUnit.MINUTES.between(theUpdatedDate, now);

        return minutes > 5;
    }
}
