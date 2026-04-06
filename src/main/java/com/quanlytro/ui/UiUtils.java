package com.quanlytro.ui;

import javax.swing.JOptionPane;
import java.awt.Component;

public final class UiUtils {

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Loi", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thong bao", JOptionPane.INFORMATION_MESSAGE);
    }

    private UiUtils() {
    }
}
