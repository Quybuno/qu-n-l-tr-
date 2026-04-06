package com.quanlytro.ui;

import com.quanlytro.controller.ChiSoController;
import com.quanlytro.controller.HoaDonController;
import com.quanlytro.controller.HopDongController;
import com.quanlytro.controller.KhachController;
import com.quanlytro.controller.PhongController;
import com.quanlytro.controller.ThongKeController;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;

public class MainFrame extends JFrame {

    public MainFrame(PhongController phongController,
                     KhachController khachController,
                     HopDongController hopDongController,
                     ChiSoController chiSoController,
                     HoaDonController hoaDonController,
                     ThongKeController thongKeController) {
        super("Quan ly phong tro");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(960, 620);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Phong", new PhongPanel(phongController));
        tabs.addTab("Khach thue", new KhachPanel(khachController));
        tabs.addTab("Hop dong", new HopDongPanel(hopDongController, thongKeController, khachController));
        tabs.addTab("Chi so D/N", new ChiSoPanel(chiSoController, hopDongController));
        tabs.addTab("Hoa don", new HoaDonPanel(hoaDonController, hopDongController));
        tabs.addTab("Thong ke", new ThongKePanel(thongKeController));

        tabs.addChangeListener(e -> {
            Component c = tabs.getSelectedComponent();
            if (c instanceof Refreshable r) {
                r.refreshData();
            }
        });

        add(tabs, BorderLayout.CENTER);
    }

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }
}
