package com.quanlytro.ui;

import com.quanlytro.controller.HopDongController;
import com.quanlytro.controller.KhachController;
import com.quanlytro.controller.ThongKeController;
import com.quanlytro.model.HopDong;
import com.quanlytro.model.NguoiThue;
import com.quanlytro.model.PhongTro;
import com.quanlytro.utils.AppConstant;
import com.quanlytro.utils.DateUtils;

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

public class HopDongPanel extends JPanel implements Refreshable {

    private final HopDongController hopDongController;
    private final ThongKeController thongKeController;
    private final KhachController khachController;

    private final DefaultTableModel tableModel;
    private final JComboBox<PhongTro> cbPhong = new JComboBox<>();
    private final JComboBox<NguoiThue> cbNguoi = new JComboBox<>();
    private final JTextField tfMaHd = new JTextField(12);
    private final JTextField tfBatDau = new JTextField(12);
    private final JTextField tfKetThuc = new JTextField(12);
    private final JTextField tfCoc = new JTextField(10);
    private final JTextField tfGiaDien = new JTextField("3500", 8);
    private final JTextField tfGiaNuoc = new JTextField("18000", 8);

    public HopDongPanel(HopDongController hopDongController,
                        ThongKeController thongKeController,
                        KhachController khachController) {
        this.hopDongController = hopDongController;
        this.thongKeController = thongKeController;
        this.khachController = khachController;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] cols = {"Ma HD", "Phong", "Nguoi thue", "Bat dau", "Ket thuc", "Coc", "ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        cbPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("");
                } else if (value instanceof PhongTro p) {
                    setText(p.getMaPhong() + " — " + p.getId());
                }
                return this;
            }
        });
        cbNguoi.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("");
                } else if (value instanceof NguoiThue n) {
                    setText(n.getHoTen() + " — " + n.getId());
                }
                return this;
            }
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Lap hop dong"));
        form.add(new JLabel("Ma hop dong:"));
        form.add(tfMaHd);
        form.add(new JLabel("Phong trong (chi phong TRONG):"));
        form.add(cbPhong);
        form.add(new JLabel("Nguoi thue (tat ca khach):"));
        form.add(cbNguoi);
        form.add(new JLabel("Ngay bat dau (" + AppConstant.DATE_PATTERN + "):"));
        form.add(tfBatDau);
        form.add(new JLabel("Ngay ket thuc (tuy chon):"));
        form.add(tfKetThuc);
        form.add(new JLabel("Tien coc:"));
        form.add(tfCoc);
        form.add(new JLabel("Gia dien / so (VND):"));
        form.add(tfGiaDien);
        form.add(new JLabel("Gia nuoc / khoi (VND):"));
        form.add(tfGiaNuoc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTao = new JButton("Tao hop dong");
        btnTao.addActionListener(e -> taoHopDong());
        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refreshAll());
        actions.add(btnTao);
        actions.add(btnTai);

        JLabel hint = new JLabel(
                "<html><body style='width:520px'><small>"
                        + "<b>Goi y:</b> Combo phong chi hien phong con <b>TRONG</b> (phong da thue se khong co). "
                        + "Bang duoi la <b>hop dong da lap</b>, khong phai danh sach phong hay khach."
                        + "</small></body></html>");
        JPanel north = new JPanel(new BorderLayout(0, 8));
        north.add(hint, BorderLayout.NORTH);
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshAll();
        UiUtils.refreshWhenPanelShown(this, this::refreshData);
    }

    @Override
    public void refreshData() {
        refreshAll();
    }

    private void refreshCombos() {
        cbPhong.setModel(new DefaultComboBoxModel<>(thongKeController.phongDangTrong().toArray(new PhongTro[0])));
        cbNguoi.setModel(new DefaultComboBoxModel<>(khachController.danhSachKhach().toArray(new NguoiThue[0])));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (HopDong h : hopDongController.danhSachHopDong()) {
            String phong = h.getPhongTro() != null ? h.getPhongTro().getMaPhong() : h.getPhongTroId();
            String nguoi = h.getNguoiThue() != null ? h.getNguoiThue().getHoTen() : h.getNguoiThueId();
            tableModel.addRow(new Object[]{
                    h.getMaHopDong(),
                    phong,
                    nguoi,
                    DateUtils.format(h.getNgayBatDau()),
                    h.getNgayKetThuc() != null ? DateUtils.format(h.getNgayKetThuc()) : "",
                    h.getTienCoc() != null ? h.getTienCoc().toString() : "0",
                    h.getId()
            });
        }
    }

    private void refreshAll() {
        refreshCombos();
        refreshTable();
    }

    private void taoHopDong() {
        PhongTro p = (PhongTro) cbPhong.getSelectedItem();
        NguoiThue n = (NguoiThue) cbNguoi.getSelectedItem();
        if (p == null) {
            UiUtils.error(this, "Khong co phong trong.");
            return;
        }
        if (n == null) {
            UiUtils.error(this, "Chua co nguoi thue trong danh sach.");
            return;
        }
        if (!DateUtils.isValidDateString(tfBatDau.getText())) {
            UiUtils.error(this, "Ngay bat dau khong hop le.");
            return;
        }
        String kt = tfKetThuc.getText().trim();
        if (!kt.isEmpty() && !DateUtils.isValidDateString(kt)) {
            UiUtils.error(this, "Ngay ket thuc khong hop le.");
            return;
        }
        BigDecimal coc;
        try {
            String c = tfCoc.getText().trim();
            coc = c.isEmpty() ? BigDecimal.ZERO : new BigDecimal(c);
        } catch (Exception ex) {
            UiUtils.error(this, "Tien coc khong hop le.");
            return;
        }

        HopDong hd = new HopDong();
        hd.setMaHopDong(tfMaHd.getText());
        hd.setPhongTroId(p.getId());
        hd.setNguoiThueId(n.getId());
        hd.setNgayBatDau(DateUtils.parse(tfBatDau.getText().trim()));
        hd.setNgayKetThuc(kt.isEmpty() ? null : DateUtils.parse(kt));
        hd.setTienCoc(coc);
        try {
            String gd = tfGiaDien.getText().trim();
            String gn = tfGiaNuoc.getText().trim();
            if (!gd.isEmpty()) {
                hd.setGiaDienMoiSo(new BigDecimal(gd));
            }
            if (!gn.isEmpty()) {
                hd.setGiaNuocMoiKhoi(new BigDecimal(gn));
            }
        } catch (Exception ex) {
            UiUtils.error(this, "Don gia dien/nuoc khong hop le.");
            return;
        }

        String err = hopDongController.taoHopDong(hd);
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Tao hop dong thanh cong.");
            tfMaHd.setText("");
            tfBatDau.setText("");
            tfKetThuc.setText("");
            tfCoc.setText("");
            tfGiaDien.setText("3500");
            tfGiaNuoc.setText("18000");
            refreshAll();
        }
    }
}
