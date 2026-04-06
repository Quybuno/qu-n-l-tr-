package com.quanlytro.ui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.HierarchyEvent;

public final class UiUtils {

    /** Goi refresh moi khi panel trong tab duoc hien thi (on dinh hon chi dung ChangeListener). */
    public static void refreshWhenPanelShown(JPanel panel, Runnable onRefresh) {
        panel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && panel.isShowing()) {
                onRefresh.run();
            }
        });
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Loi", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thong bao", JOptionPane.INFORMATION_MESSAGE);
    }

    private UiUtils() {
    }
}
