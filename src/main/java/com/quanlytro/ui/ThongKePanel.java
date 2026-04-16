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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ThongKePanel extends JPanel implements Refreshable {

    private final ThongKeController thongKeController;
    private final JLabel lbPhongTrong = new JLabel("0");
    private final JLabel lbDoanhThu = new JLabel("-");
    private final JTextField tfNam = new JTextField("2026", 6);
    private final JTextField tfThang = new JTextField("4", 4);
    private final JTextField tfThangDen = new JTextField("4", 4);
    private final DefaultTableModel unpaidModel;
    private final DefaultTableModel paidModel;

    public ThongKePanel(ThongKeController thongKeController) {
        this.thongKeController = thongKeController;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new GridLayout(0, 2, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("So lieu nhanh (theo day tro dang chon)"));
        top.add(new JLabel("So phong trong:"));
        top.add(lbPhongTrong);

        JPanel doanhThuInputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doanhThuInputs.add(new JLabel("Nam:"));
        doanhThuInputs.add(tfNam);
        doanhThuInputs.add(new JLabel("Tu:"));
        doanhThuInputs.add(tfThang);
        doanhThuInputs.add(new JLabel("Den:"));
        doanhThuInputs.add(tfThangDen);

        JPanel doanhThuButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTinh = new JButton("Tinh doanh thu theo thang");
        btnTinh.addActionListener(e -> tinhDoanhThuTheoKy());
        // JButton btnTinhNam = new JButton("Tong doanh thu nam");
        // btnTinhNam.addActionListener(e -> tinhDoanhThuNam());
        doanhThuButtons.add(btnTinh);
        // doanhThuButtons.add(btnTinhNam);

        JPanel doanhThuWrap = new JPanel();
        doanhThuWrap.setLayout(new BoxLayout(doanhThuWrap, BoxLayout.Y_AXIS));
        doanhThuWrap.add(doanhThuInputs);
        doanhThuWrap.add(doanhThuButtons);
        top.add(new JLabel("Doanh thu theo ky:"));
        top.add(doanhThuWrap);
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
        installGroupedNumberRenderer(unpaidTable, 2);

        String[] paidCols = {"Ma phong", "Ma hop dong", "Ma hoa don", "Ky", "So tien"};
        paidModel = new DefaultTableModel(paidCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable paidTable = new JTable(paidModel);
        paidTable.setFillsViewportHeight(true);
        installGroupedNumberRenderer(paidTable, 4);

        JPanel unpaidWrap = new JPanel(new BorderLayout());
        unpaidWrap.setBorder(BorderFactory.createTitledBorder("Hoa don chua thanh toan (tat ca ky, trong day)"));
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

    private void tinhDoanhThuTheoKy() {
        try {
            int nam = Integer.parseInt(tfNam.getText().trim());
            int tu = Integer.parseInt(tfThang.getText().trim());
            int den = Integer.parseInt(tfThangDen.getText().trim());
            BigDecimal tong = thongKeController.tongDoanhThuNhieuThang(nam, tu, den);
            lbDoanhThu.setText(formatMoney(tong));
            fillPaidTableRange(nam, tu, den);
        } catch (IllegalArgumentException ex) {
            UiUtils.error(this, "Khoang thang khong hop le (1-12, thang den khong duoc nho hon thang bat dau).");
        } catch (Exception ex) {
            UiUtils.error(this, "Nam / thang khong hop le.");
        }
    }

    private void tinhDoanhThuNam() {
        try {
            int nam = Integer.parseInt(tfNam.getText().trim());
            BigDecimal tong = thongKeController.tongDoanhThuNam(nam);
            lbDoanhThu.setText(formatMoney(tong));
        } catch (Exception ex) {
            UiUtils.error(this, "Nam khong hop le.");
        }
    }

    private void refresh() {
        lbPhongTrong.setText(String.valueOf(thongKeController.demPhongTrong()));
        unpaidModel.setRowCount(0);
        for (HoaDon h : thongKeController.hoaDonChuaThanhToan()) {
            unpaidModel.addRow(new Object[]{
                    h.getMaHoaDon(),
                    String.format("%02d/%d", h.getThang(), h.getNam()),
                    h.getTongTien()
            });
        }
        try {
            int nam = Integer.parseInt(tfNam.getText().trim());
            int tu = Integer.parseInt(tfThang.getText().trim());
            int den = Integer.parseInt(tfThangDen.getText().trim());
            fillPaidTableRange(nam, tu, den);
        } catch (Exception ex) {
            paidModel.setRowCount(0);
        }
    }

    private void fillPaidTableRange(int nam, int tu, int den) {
        paidModel.setRowCount(0);
        for (HoaDon h : thongKeController.hoaDonDaThanhToanTheoKhoangThang(nam, tu, den)) {
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
                    h.getTongTien()
            });
        }
    }

    private static String formatMoney(BigDecimal v) {
        if (v == null) {
            return "-";
        }
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance(new Locale("vi", "VN"));
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        DecimalFormat fmt = new DecimalFormat("#,##0.####", sym);
        fmt.setRoundingMode(RoundingMode.DOWN);
        return fmt.format(v.stripTrailingZeros());
    }

    private static void installGroupedNumberRenderer(JTable table, int... modelCols) {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance(new Locale("vi", "VN"));
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        DecimalFormat fmt = new DecimalFormat("#,##0.####", sym);
        fmt.setRoundingMode(RoundingMode.DOWN);

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value == null) {
                    setText("");
                    return;
                }
                try {
                    BigDecimal bd = value instanceof BigDecimal b
                            ? b
                            : new BigDecimal(String.valueOf(value).trim());
                    setText(fmt.format(bd.stripTrailingZeros()));
                } catch (Exception ex) {
                    setText(String.valueOf(value));
                }
            }
        };

        for (int c : modelCols) {
            int viewCol = table.convertColumnIndexToView(c);
            if (viewCol >= 0 && viewCol < table.getColumnModel().getColumnCount()) {
                table.getColumnModel().getColumn(viewCol).setCellRenderer(r);
            }
        }
    }
}
