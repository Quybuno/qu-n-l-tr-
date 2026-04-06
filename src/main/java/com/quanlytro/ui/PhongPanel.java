package com.quanlytro.ui;

import com.quanlytro.controller.PhongController;
import com.quanlytro.model.PhongTro;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;

public class PhongPanel extends JPanel implements Refreshable {

    private final PhongController phongController;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField tfMa = new JTextField(12);
    private final JTextField tfDienTich = new JTextField(8);
    private final JTextField tfGia = new JTextField(10);

    public PhongPanel(PhongController phongController) {
        this.phongController = phongController;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] cols = {"Ma phong", "Dien tich", "Gia thue", "Trang thai", "ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Them phong"));
        form.add(new JLabel("Ma phong:"));
        form.add(tfMa);
        form.add(new JLabel("Dien tich (tuy chon):"));
        form.add(tfDienTich);
        form.add(new JLabel("Gia thue / thang:"));
        form.add(tfGia);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Them phong");
        btnThem.addActionListener(e -> themPhong());
        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refresh());
        JButton btnGia = new JButton("Cap nhat gia (chon dong)");
        btnGia.addActionListener(e -> capNhatGia());
        actions.add(btnThem);
        actions.add(btnTai);
        actions.add(btnGia);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh();
        UiUtils.refreshWhenPanelShown(this, this::refreshData);
    }

    @Override
    public void refreshData() {
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        for (PhongTro p : phongController.danhSachPhong()) {
            tableModel.addRow(new Object[]{
                    p.getMaPhong(),
                    p.getDienTich() != null ? p.getDienTich().toString() : "",
                    p.getGiaThueThang() != null ? p.getGiaThueThang().toString() : "",
                    p.getTrangThai() != null ? p.getTrangThai().name() : "",
                    p.getId()
            });
        }
    }

    private void themPhong() {
        BigDecimal dienTich = null;
        String dt = tfDienTich.getText().trim();
        if (!dt.isEmpty()) {
            try {
                dienTich = new BigDecimal(dt);
            } catch (Exception ex) {
                UiUtils.error(this, "Dien tich khong hop le.");
                return;
            }
        }
        BigDecimal gia;
        try {
            gia = new BigDecimal(tfGia.getText().trim());
        } catch (Exception ex) {
            UiUtils.error(this, "Gia thue khong hop le.");
            return;
        }
        String err = phongController.themPhong(tfMa.getText(), dienTich, gia);
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Them phong thanh cong.");
            tfMa.setText("");
            tfDienTich.setText("");
            tfGia.setText("");
            refresh();
        }
    }

    private void capNhatGia() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtils.error(this, "Chon mot dong trong bang.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 4);
        String input = JOptionPane.showInputDialog(this, "Gia thue moi:", "Cap nhat gia",
                JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return;
        }
        try {
            BigDecimal gia = new BigDecimal(input.trim());
            String err = phongController.capNhatGiaPhong(id, gia);
            if (err != null) {
                UiUtils.error(this, err);
            } else {
                UiUtils.info(this, "Cap nhat thanh cong.");
                refresh();
            }
        } catch (Exception ex) {
            UiUtils.error(this, "Gia khong hop le.");
        }
    }
}
