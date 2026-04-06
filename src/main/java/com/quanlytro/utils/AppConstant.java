package com.quanlytro.utils;

import java.math.BigDecimal;

public final class AppConstant {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /** Don gia mac dinh: VND / so dien (kWh) */
    public static final BigDecimal DEFAULT_GIA_DIEN_MOI_SO = new BigDecimal("3500");
    /** Don gia mac dinh: VND / khoi nuoc (m3) */
    public static final BigDecimal DEFAULT_GIA_NUOC_MOI_KHOI = new BigDecimal("18000");

    private AppConstant() {
    }
}
