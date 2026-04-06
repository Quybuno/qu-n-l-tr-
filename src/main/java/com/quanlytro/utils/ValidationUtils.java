package com.quanlytro.utils;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern CMND = Pattern.compile("^\\d{9}$|^\\d{12}$");
    private static final Pattern PHONE = Pattern.compile("^0\\d{9,10}$");

    public static boolean isNonBlank(String s) {
        return s != null && !s.isBlank();
    }

    public static boolean isCmnd(String cmnd) {
        return cmnd != null && CMND.matcher(cmnd.trim()).matches();
    }

    public static boolean isPhone(String phone) {
        return phone != null && PHONE.matcher(phone.trim()).matches();
    }

    private ValidationUtils() {
    }
}
