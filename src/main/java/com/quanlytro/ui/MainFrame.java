package com.quanlytro.ui;

import com.quanlytro.context.DayTroContext;
import com.quanlytro.controller.ChiSoController;
import com.quanlytro.controller.DayTroController;
import com.quanlytro.controller.HoaDonController;
import com.quanlytro.controller.HopDongController;
import com.quanlytro.controller.KhachController;
import com.quanlytro.controller.PhongController;
import com.quanlytro.controller.ThongKeController;
import com.quanlytro.model.DayTro;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

public class MainFrame extends JFrame {

    private final DayTroController dayTroController;
    private final JComboBox<DayTro> cbDay = new JComboBox<>();

    private final PhongPanel phongPanel;
    private final HopDongPanel hopDongPanel;
    private final ChiSoPanel chiSoPanel;
    private final HoaDonPanel hoaDonPanel;
    private final ThongKePanel thongKePanel;

    public MainFrame(PhongController phongController,
                     KhachController khachController,
                     HopDongController hopDongController,
                     ChiSoController chiSoController,
                     HoaDonController hoaDonController,
                     ThongKeController thongKeController,
                     DayTroController dayTroController) {
        super("Quan ly phong tro — nhieu day tro");
        this.dayTroController = dayTroController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);

        cbDay.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DayTro d) {
                    setText(d.getTenDay() + " (" + d.getMaDay() + ")");
                } else {
                    setText("");
                }
                return this;
            }
        });
        cbDay.addActionListener(e -> {
            DayTro d = (DayTro) cbDay.getSelectedItem();
            if (d != null) {
                DayTroContext.setSelectedDayTroId(d.getId());
                refreshAllPanels();
            }
        });

        phongPanel = new PhongPanel(phongController);
        DayTroPanel dayTroPanel = new DayTroPanel(dayTroController, this::reloadDayComboAndRefresh);

        hopDongPanel = new HopDongPanel(hopDongController, thongKeController, khachController);
        chiSoPanel = new ChiSoPanel(chiSoController, hopDongController);
        hoaDonPanel = new HoaDonPanel(hoaDonController, hopDongController);
        thongKePanel = new ThongKePanel(thongKeController);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Phong", phongPanel);
        tabs.addTab("Khach thue", new KhachPanel(khachController));
        tabs.addTab("Hop dong", hopDongPanel);
        tabs.addTab("Chi so D/N", chiSoPanel);
        tabs.addTab("Hoa don", hoaDonPanel);
        tabs.addTab("Thong ke", thongKePanel);
        tabs.addTab("Day tro", dayTroPanel);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Day tro dang lam viec:"));
        top.add(cbDay);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        reloadDayComboAndRefresh();
    }

    private void reloadDayComboAndRefresh() {
        List<DayTro> list = dayTroController.danhSachDayTro();
        String prev = DayTroContext.getSelectedDayTroId();
        cbDay.setModel(new DefaultComboBoxModel<>(list.toArray(new DayTro[0])));

        DayTro keep = null;
        if (prev != null) {
            for (DayTro d : list) {
                if (d.getId().equals(prev)) {
                    keep = d;
                    break;
                }
            }
        }
        if (keep != null) {
            cbDay.setSelectedItem(keep);
            DayTroContext.setSelectedDayTroId(keep.getId());
        } else if (!list.isEmpty()) {
            cbDay.setSelectedIndex(0);
            DayTroContext.setSelectedDayTroId(list.get(0).getId());
        } else {
            DayTroContext.setSelectedDayTroId(null);
        }
        refreshAllPanels();
    }

    private void refreshAllPanels() {
        phongPanel.refreshData();
        hopDongPanel.refreshData();
        chiSoPanel.refreshData();
        hoaDonPanel.refreshData();
        thongKePanel.refreshData();
    }

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }
}
