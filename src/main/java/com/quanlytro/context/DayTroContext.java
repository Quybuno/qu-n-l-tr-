package com.quanlytro.context;

/**
 * Dãy trọ đang được chọn trên giao diện (combo trên cửa sổ chính).
 */
public final class DayTroContext {

    private static volatile String selectedDayTroId;

    private DayTroContext() {
    }

    public static void setSelectedDayTroId(String id) {
        selectedDayTroId = id;
    }

    public static String getSelectedDayTroId() {
        return selectedDayTroId;
    }
}
