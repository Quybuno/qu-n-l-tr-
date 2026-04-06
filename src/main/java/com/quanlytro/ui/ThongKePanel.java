package com.quanlytro.ui;

import com.quanlytro.controller.ThongKeController;
import com.quanlytro.model.HoaDon;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;

public class ThongKePanel extends JPanel implements Refreshable {

    private final ThongKeController thongKeController;
    private final JLabel lbPhongTrong = new JLabel("0");
    private final JLabel lbDoanhThu = new JLabel("-");
    private final JTextField tfNam = new JTextField("2026", 6);
    private final JTextField tfThang = new JTextField("4", 4);
    private final DefaultTableModel unpaidModel;

    public ThongKePanel(ThongKeController thongKeController) {
        this.thongKeController = thongKeController;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new GridLayout(0, 2, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("So lieu nhanh"));
        top.add(new JLabel("So phong trong:"));
        top.add(lbPhongTrong);

        JPanel doanhThuRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doanhThuRow.add(new JLabel("Nam:"));
        doanhThuRow.add(tfNam);
        doanhThuRow.add(new JLabel("Thang:"));
        doanhThuRow.add(tfThang);
        JButton btnTinh = new JButton("Tong doanh thu da thanh toan");
        btnTinh.addActionListener(e -> tinhDoanhThu());
        doanhThuRow.add(btnTinh);
        top.add(new JLabel("Doanh thu theo ky:"));
        top.add(doanhThuRow);
        top.add(new JLabel("Ket qua:"));
        top.add(lbDoanhThu);

        String[] cols = {"Ma HD", "Ky", "So tien"};
        unpaidModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable unpaidTable = new JTable(unpaidModel);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Hoa don chua thanh toan"));
        center.add(new JScrollPane(unpaidTable), BorderLayout.CENTER);

        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refresh());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.add(btnTai);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        refresh();
    }

    @Override
    public void refreshData() {
        refresh();
    }

    private void tinhDoanhThu() {
        try {
            int nam = Integer.parseInt(tfNam.getText().trim());
            int thang = Integer.parseInt(tfThang.getText().trim());
            BigDecimal tong = thongKeController.tongDoanhThuThang(nam, thang);
            lbDoanhThu.setText(tong.toPlainString());
        } catch (Exception ex) {
            UiUtils.error(this, "Nam / thang khong hop le.");
        }
    }

    private void refresh() {
        lbPhongTrong.setText(String.valueOf(thongKeController.demPhongTrong()));
        unpaidModel.setRowCount(0);
        for (HoaDon h : thongKeController.hoaDonChuaThanhToan()) {
            unpaidModel.addRow(new Object[]{
                    h.getMaHoaDon(),
                    String.format("%02d/%d", h.getThang(), h.getNam()),
                    h.getTongTien() != null ? h.getTongTien().toString() : ""
            });
        }
    }
}
