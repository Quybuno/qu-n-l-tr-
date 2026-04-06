package com.quanlytro.ui;

import com.quanlytro.controller.HoaDonController;
import com.quanlytro.controller.HopDongController;
import com.quanlytro.model.HoaDon;
import com.quanlytro.model.HopDong;
import com.quanlytro.model.enums.TrangThaiHoaDon;

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

public class HoaDonPanel extends JPanel implements Refreshable {

    private final HoaDonController hoaDonController;
    private final HopDongController hopDongController;

    private final DefaultTableModel tableModel;
    private final JComboBox<HopDong> cbHopDong = new JComboBox<>();

    private final JTextField tfMa = new JTextField(12);
    private final JTextField tfNam = new JTextField(6);
    private final JTextField tfThang = new JTextField(4);
    private final JTextField tfTp = new JTextField(10);
    private final JTextField tfTd = new JTextField(10);
    private final JTextField tfTn = new JTextField(10);

    public HoaDonPanel(HoaDonController hoaDonController, HopDongController hopDongController) {
        this.hoaDonController = hoaDonController;
        this.hopDongController = hopDongController;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] cols = {"Ma HD", "Ky", "Tong", "Trang thai", "Ma hop dong", "ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

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

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Tao hoa don"));
        form.add(new JLabel("Hop dong:"));
        form.add(cbHopDong);
        form.add(new JLabel("Ma hoa don:"));
        form.add(tfMa);
        form.add(new JLabel("Nam:"));
        form.add(tfNam);
        form.add(new JLabel("Thang (1-12):"));
        form.add(tfThang);
        form.add(new JLabel("Tien phong:"));
        form.add(tfTp);
        form.add(new JLabel("Tien dien:"));
        form.add(tfTd);
        form.add(new JLabel("Tien nuoc:"));
        form.add(tfTn);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTao = new JButton("Tao hoa don");
        btnTao.addActionListener(e -> taoHoaDon());
        JButton btnTuChiSo = new JButton("Dien tu chi so + phong");
        btnTuChiSo.addActionListener(e -> dienTuChiSo());
        JButton btnTt = new JButton("Danh dau da thanh toan (chon dong)");
        btnTt.addActionListener(e -> danhDauThanhToan(table));
        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refreshAll());
        actions.add(btnTao);
        actions.add(btnTuChiSo);
        actions.add(btnTt);
        actions.add(btnTai);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshAll();
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
        for (HoaDon h : hoaDonController.danhSachHoaDon()) {
            String hdMa = h.getHopDong() != null ? h.getHopDong().getMaHopDong() : h.getHopDongId();
            tableModel.addRow(new Object[]{
                    h.getMaHoaDon(),
                    String.format("%02d/%d", h.getThang(), h.getNam()),
                    h.getTongTien() != null ? h.getTongTien().toString() : "",
                    h.getTrangThai() != null ? h.getTrangThai().name() : "",
                    hdMa,
                    h.getId()
            });
        }
    }

    private void refreshAll() {
        refreshCombos();
        refreshTable();
    }

    /** Lay tien phong tu gia thang; tien dien/nuoc tu chi so ky (tab Chi so). */
    private void dienTuChiSo() {
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
        BigDecimal tp = hoaDonController.layTienPhongTheoHopDong(hd.getId());
        if (tp == null) {
            UiUtils.error(this, "Khong lay duoc gia phong.");
            return;
        }
        BigDecimal[] dn = hoaDonController.tinhTienDienNuocTuChiSo(hd.getId(), nam, thang);
        if (dn == null) {
            UiUtils.error(this, "Chua co chi so dien nuoc cho ky nay, hoac chi so khong hop le.");
            return;
        }
        tfTp.setText(tp.toPlainString());
        tfTd.setText(dn[0].toPlainString());
        tfTn.setText(dn[1].toPlainString());
        UiUtils.info(this, "Da dien tien phong + dien + nuoc. Kiem tra va bam Tao hoa don.");
    }

    private void taoHoaDon() {
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
        BigDecimal tp;
        BigDecimal td;
        BigDecimal tn;
        try {
            tp = new BigDecimal(tfTp.getText().trim());
            td = new BigDecimal(tfTd.getText().trim());
            tn = new BigDecimal(tfTn.getText().trim());
        } catch (Exception ex) {
            UiUtils.error(this, "So tien khong hop le.");
            return;
        }

        HoaDon bill = new HoaDon();
        bill.setMaHoaDon(tfMa.getText().trim());
        bill.setHopDongId(hd.getId());
        bill.setNam(nam);
        bill.setThang(thang);
        bill.setTienPhong(tp);
        bill.setTienDien(td);
        bill.setTienNuoc(tn);
        bill.setTrangThai(TrangThaiHoaDon.CHUA_THANH_TOAN);

        String err = hoaDonController.taoHoaDon(bill);
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Tao hoa don thanh cong.");
            tfMa.setText("");
            refreshAll();
        }
    }

    private void danhDauThanhToan(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtils.error(this, "Chon mot dong hoa don.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 5);
        String err = hoaDonController.danhDauDaThanhToan(id);
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Da cap nhat trang thai.");
            refreshAll();
        }
    }
}
