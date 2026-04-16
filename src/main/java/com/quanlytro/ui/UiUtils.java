package com.quanlytro.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.HierarchyEvent;

public final class UiUtils {

//    Goi refresh moi khi panel trong tab duoc hien thi
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

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        Border outer = BorderFactory.createLineBorder(new Color(229, 231, 235));
        Border inner = BorderFactory.createEmptyBorder(12, 12, 12, 12);
        card.setBorder(BorderFactory.createCompoundBorder(outer, inner));
        return card;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(new Color(17, 24, 39));

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setBackground(new Color(248, 250, 252));
            header.setForeground(new Color(55, 65, 81));
            header.setFont(header.getFont().deriveFont(Font.BOLD));
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));
            header.setReorderingAllowed(false);
        }
    }

    public static void styleTextField(JTextField textField) {
        textField.setMargin(new Insets(6, 8, 6, 8));
    }

    public static void stylePrimaryButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(new Color(17, 24, 39));
        button.setForeground(Color.WHITE);
        button.setMargin(new Insets(8, 12, 8, 12));
    }

    public static void styleGhostButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(UIManager.getColor("Panel.background"));
        button.setForeground(new Color(31, 41, 55));
        button.setMargin(new Insets(8, 12, 8, 12));
    }

    public static void styleTitleLabel(JComponent label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD, 24f));
        if (label instanceof javax.swing.JLabel jLabel) {
            jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    private UiUtils() {
    }
}
