package com.quanlytro.ui;

import com.quanlytro.controller.ThongKeController;
import com.quanlytro.model.HoaDon;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
    private final DefaultTableModel paidModel;

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

        String[] unpaidCols = {"Ma HD", "Ky", "So tien"};
        unpaidModel = new DefaultTableModel(unpaidCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable unpaidTable = new JTable(unpaidModel);
        unpaidTable.setFillsViewportHeight(true);

        String[] paidCols = {"Ma phong", "Ma hop dong", "Ma hoa don", "Ky", "So tien"};
        paidModel = new DefaultTableModel(paidCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable paidTable = new JTable(paidModel);
        paidTable.setFillsViewportHeight(true);

        JPanel unpaidWrap = new JPanel(new BorderLayout());
        unpaidWrap.setBorder(BorderFactory.createTitledBorder("Hoa don chua thanh toan (tat ca ky)"));
        unpaidWrap.add(new JScrollPane(unpaidTable), BorderLayout.CENTER);
        unpaidWrap.setPreferredSize(new Dimension(0, 160));

        JPanel paidWrap = new JPanel(new BorderLayout());
        paidWrap.setBorder(BorderFactory.createTitledBorder(
                "Phong / hoa don da thanh toan (theo nam & thang o tren)"));
        paidWrap.add(new JScrollPane(paidTable), BorderLayout.CENTER);
        paidWrap.setPreferredSize(new Dimension(0, 180));

        JPanel tables = new JPanel();
        tables.setLayout(new BoxLayout(tables, BoxLayout.Y_AXIS));
        tables.add(unpaidWrap);
        tables.add(paidWrap);

        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refresh());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.add(btnTai);

        add(top, BorderLayout.NORTH);
        add(tables, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        refresh();
        UiUtils.refreshWhenPanelShown(this, this::refreshData);
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
            fillPaidTable(nam, thang);
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
        try {
            int nam = Integer.parseInt(tfNam.getText().trim());
            int thang = Integer.parseInt(tfThang.getText().trim());
            fillPaidTable(nam, thang);
        } catch (Exception ex) {
            paidModel.setRowCount(0);
        }
    }

    private void fillPaidTable(int nam, int thang) {
        paidModel.setRowCount(0);
        for (HoaDon h : thongKeController.hoaDonDaThanhToanTheoKy(nam, thang)) {
            String maPhong = "-";
            String maHd = h.getHopDongId();
            if (h.getHopDong() != null) {
                maHd = h.getHopDong().getMaHopDong();
                if (h.getHopDong().getPhongTro() != null) {
                    maPhong = h.getHopDong().getPhongTro().getMaPhong();
                }
            }
            paidModel.addRow(new Object[]{
                    maPhong,
                    maHd,
                    h.getMaHoaDon(),
                    String.format("%02d/%d", h.getThang(), h.getNam()),
                    h.getTongTien() != null ? h.getTongTien().toString() : ""
            });
        }
    }
}
