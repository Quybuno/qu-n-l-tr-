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

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {

    private final DayTroController dayTroController;
    private final JComboBox<DayTro> cbDay = new JComboBox<>();

    private final PhongPanel phongPanel;
    private final KhachPanel khachPanel;
    private final HopDongPanel hopDongPanel;
    private final ChiSoPanel chiSoPanel;
    private final HoaDonPanel hoaDonPanel;
    private final ThongKePanel thongKePanel;
    private final DayTroPanel dayTroPanel;
    private final JLabel lbPageTitle = new JLabel("Dashboard");

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);

    public MainFrame(PhongController phongController,
                     KhachController khachController,
                     HopDongController hopDongController,
                     ChiSoController chiSoController,
                     HoaDonController hoaDonController,
                     ThongKeController thongKeController,
                     DayTroController dayTroController) {
        super("Quan ly phong tro");
        this.dayTroController = dayTroController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
        setSize(1280, 800);
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
        khachPanel = new KhachPanel(khachController);
        hopDongPanel = new HopDongPanel(hopDongController, thongKeController, khachController);
        chiSoPanel = new ChiSoPanel(chiSoController, hopDongController);
        hoaDonPanel = new HoaDonPanel(hoaDonController, hopDongController);
        thongKePanel = new ThongKePanel(thongKeController);
        dayTroPanel = new DayTroPanel(dayTroController, this::reloadDayComboAndRefresh);

        content.add(thongKePanel, View.THONG_KE.cardKey);
        content.add(phongPanel, View.PHONG.cardKey);
        content.add(khachPanel, View.KHACH.cardKey);
        content.add(hopDongPanel, View.HOP_DONG.cardKey);
        content.add(hoaDonPanel, View.HOA_DON.cardKey);
        content.add(chiSoPanel, View.CHI_SO.cardKey);
        content.add(dayTroPanel, View.DAY_TRO.cardKey);

        JPanel topbar = buildTopbar();
        JPanel sidebar = buildSidebar();

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        centerWrap.add(topbar, BorderLayout.NORTH);
        centerWrap.add(content, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(centerWrap, BorderLayout.CENTER);

        reloadDayComboAndRefresh();
        showView(View.THONG_KE);
    }

    private JPanel buildTopbar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        lbPageTitle.setText("Dashboard");
        UiUtils.styleTitleLabel(lbPageTitle);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(lbPageTitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(new JLabel("Day tro:"));
        right.add(cbDay);

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        top.setOpaque(false);
        return top;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(245, 246, 248));

        JLabel brand = new JLabel("Quan ly phong tro");
        brand.setFont(brand.getFont().deriveFont(Font.BOLD, 14f));
        brand.setBorder(BorderFactory.createEmptyBorder(0, 4, 12, 4));
        sidebar.add(brand);

        sidebar.add(navButton(View.THONG_KE, "Trang chu"));
        sidebar.add(navButton(View.PHONG, "Phong"));
        sidebar.add(navButton(View.KHACH, "Khach thue"));
        sidebar.add(navButton(View.HOP_DONG, "Hop dong"));
        sidebar.add(navButton(View.CHI_SO, "Chi so dien/nuoc"));
        sidebar.add(navButton(View.HOA_DON, "Hoa don"));

        sidebar.add(navButton(View.DAY_TRO, "Toa nha / Day tro"));

        sidebar.add(javax.swing.Box.createVerticalGlue());

        JButton btnReload = new JButton("Tai lai du lieu");
        btnReload.setAlignmentX(Component.LEFT_ALIGNMENT);
        UiUtils.styleGhostButton(btnReload);
        btnReload.addActionListener(e -> refreshAllPanels());
        sidebar.add(btnReload);

        return sidebar;
    }

    private JButton navButton(View view, String label) {
        JButton b = new JButton(label);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        UiUtils.styleGhostButton(b);
        b.setHorizontalAlignment(JButton.LEFT);
        b.setMargin(new Insets(10, 12, 10, 12));
        b.addActionListener((ActionEvent e) -> showView(view));
        return b;
    }

    private void showView(View view) {
        cardLayout.show(content, view.cardKey);
        lbPageTitle.setText(view.title);
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
//      luu lai id cua day tro dang duoc chon de hien thi thong tin cua day do
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
        khachPanel.refreshData();
        hopDongPanel.refreshData();
        chiSoPanel.refreshData();
        hoaDonPanel.refreshData();
        thongKePanel.refreshData();
        dayTroPanel.refreshData();
    }

    public static void applyLookAndFeel() {
        try {
            // Prefer Nimbus for a more modern default UI (no extra dependency).
            boolean nimbusSet = false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    nimbusSet = true;
                    break;
                }
            }
            if (!nimbusSet) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception ignored) {
        }
    }

    private enum View {
        THONG_KE("thong_ke"),
        PHONG("phong"),
        KHACH("khach"),
        HOP_DONG("hop_dong"),
        HOA_DON("hoa_don"),
        CHI_SO("chi_so"),
        DAY_TRO("day_tro");

        final String cardKey;
        final String title;

        View(String cardKey) {
            this.cardKey = cardKey;
            this.title = switch (cardKey) {
                case "thong_ke" -> "Dashboard";
                case "phong" -> "Quan ly phong";
                case "khach" -> "Quan ly khach thue";
                case "hop_dong" -> "Quan ly hop dong";
                case "hoa_don" -> "Quan ly hoa don";
                case "chi_so" -> "Chi so dien nuoc";
                case "day_tro" -> "Quan ly toa nha / day tro";
                default -> "Dashboard";
            };
        }
    }
}
