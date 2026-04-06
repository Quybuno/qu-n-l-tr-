package com.quanlytro.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtils {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern(AppConstant.DATE_PATTERN);

    public static LocalDate parse(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return LocalDate.parse(text.trim(), FMT);
    }

    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(FMT);
    }

    public static boolean isValidDateString(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        try {
            LocalDate.parse(text.trim(), FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private DateUtils() {
    }
}
