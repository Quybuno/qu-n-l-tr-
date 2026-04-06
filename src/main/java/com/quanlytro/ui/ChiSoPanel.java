package com.quanlytro.ui;

import com.quanlytro.controller.ChiSoController;
import com.quanlytro.controller.HopDongController;
import com.quanlytro.model.ChiSoDienNuoc;
import com.quanlytro.model.HopDong;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;

public class ChiSoPanel extends JPanel implements Refreshable {

    private final ChiSoController chiSoController;
    private final HopDongController hopDongController;

    private final DefaultTableModel tableModel;
    private final JComboBox<HopDong> cbHopDong = new JComboBox<>();
    private final JTextField tfNam = new JTextField("2026", 6);
    private final JTextField tfThang = new JTextField("4", 4);
    private final JTextField tfDienCu = new JTextField(10);
    private final JTextField tfDienMoi = new JTextField(10);
    private final JTextField tfNuocCu = new JTextField(10);
    private final JTextField tfNuocMoi = new JTextField(10);

    public ChiSoPanel(ChiSoController chiSoController, HopDongController hopDongController) {
        this.chiSoController = chiSoController;
        this.hopDongController = hopDongController;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        cbHopDong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof HopDong h) {
                    setText(h.getMaHopDong() + " — " + h.getId());
                }
                return this;
            }
        });

        String[] cols = {"Ma HD", "Nam", "Thang", "Dien cu", "Dien moi", "Nuoc cu", "Nuoc moi"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Nhap chi so theo ky (thang)"));
        form.add(new JLabel("Hop dong:"));
        form.add(cbHopDong);
        form.add(new JLabel("Nam:"));
        form.add(tfNam);
        form.add(new JLabel("Thang (1-12):"));
        form.add(tfThang);
        form.add(new JLabel("Chi so dien cu:"));
        form.add(tfDienCu);
        form.add(new JLabel("Chi so dien moi:"));
        form.add(tfDienMoi);
        form.add(new JLabel("Chi so nuoc cu:"));
        form.add(tfNuocCu);
        form.add(new JLabel("Chi so nuoc moi:"));
        form.add(tfNuocMoi);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnLuu = new JButton("Luu chi so");
        btnLuu.addActionListener(e -> luuChiSo());
        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refreshAll());
        actions.add(btnLuu);
        actions.add(btnTai);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JLabel hint = new JLabel("<html><small>Don gia dien/nuoc lay tren hop dong (tab Hop dong). "
                + "Tao hoa don co the dung nut <b>Dien tu chi so + phong</b> o tab Hoa don.</small></html>");
        add(hint, BorderLayout.SOUTH);

        refreshAll();
        UiUtils.refreshWhenPanelShown(this, this::refreshData);
    }

    @Override
    public void refreshData() {
        refreshAll();
    }

    private void refreshCombos() {
        cbHopDong.setModel(new DefaultComboBoxModel<>(hopDongController.danhSachHopDong().toArray(new HopDong[0])));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (ChiSoDienNuoc c : chiSoController.danhSachTatCa()) {
            HopDong hd = chiSoController.timHopDong(c.getHopDongId());
            String ma = hd != null ? hd.getMaHopDong() : c.getHopDongId();
            tableModel.addRow(new Object[]{
                    ma,
                    c.getNam(),
                    c.getThang(),
                    c.getChiSoDienCu(),
                    c.getChiSoDienMoi(),
                    c.getChiSoNuocCu(),
                    c.getChiSoNuocMoi()
            });
        }
    }

    private void refreshAll() {
        refreshCombos();
        refreshTable();
    }

    private void luuChiSo() {
        HopDong hd = (HopDong) cbHopDong.getSelectedItem();
        if (hd == null) {
            UiUtils.error(this, "Chua co hop dong.");
            return;
        }
        int nam;
        int thang;
        try {
            nam = Integer.parseInt(tfNam.getText().trim());
            thang = Integer.parseInt(tfThang.getText().trim());
        } catch (Exception ex) {
            UiUtils.error(this, "Nam / thang khong hop le.");
            return;
        }
        BigDecimal dc;
        BigDecimal dm;
        BigDecimal nc;
        BigDecimal nm;
        try {
            dc = new BigDecimal(tfDienCu.getText().trim());
            dm = new BigDecimal(tfDienMoi.getText().trim());
            nc = new BigDecimal(tfNuocCu.getText().trim());
            nm = new BigDecimal(tfNuocMoi.getText().trim());
        } catch (Exception ex) {
            UiUtils.error(this, "Chi so khong hop le.");
            return;
        }

        ChiSoDienNuoc c = new ChiSoDienNuoc();
        c.setHopDongId(hd.getId());
        c.setNam(nam);
        c.setThang(thang);
        c.setChiSoDienCu(dc);
        c.setChiSoDienMoi(dm);
        c.setChiSoNuocCu(nc);
        c.setChiSoNuocMoi(nm);

        String err = chiSoController.luuChiSo(c);
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Da luu chi so.");
            refreshTable();
        }
    }
}
