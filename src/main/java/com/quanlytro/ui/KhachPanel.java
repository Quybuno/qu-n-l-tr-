package com.quanlytro.ui;

import com.quanlytro.controller.KhachController;
import com.quanlytro.model.NguoiThue;

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

public class KhachPanel extends JPanel implements Refreshable {

    private final KhachController khachController;
    private final DefaultTableModel tableModel;

    private final JTextField tfTen = new JTextField(20);
    private final JTextField tfCmnd = new JTextField(14);
    private final JTextField tfSdt = new JTextField(12);
    private final JTextField tfEmail = new JTextField(20);

    public KhachPanel(KhachController khachController) {
        this.khachController = khachController;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] cols = {"Ho ten", "CMND", "SDT", "Email", "ID"};
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
        form.setBorder(BorderFactory.createTitledBorder("Them khach thue"));
        form.add(new JLabel("Ho ten:"));
        form.add(tfTen);
        form.add(new JLabel("CMND/CCCD (9 hoac 12 so):"));
        form.add(tfCmnd);
        form.add(new JLabel("So dien thoai:"));
        form.add(tfSdt);
        form.add(new JLabel("Email:"));
        form.add(tfEmail);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Them khach");
        btnThem.addActionListener(e -> themKhach());
        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refresh());
        actions.add(btnThem);
        actions.add(btnTai);

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
        for (NguoiThue n : khachController.danhSachKhach()) {
            tableModel.addRow(new Object[]{
                    n.getHoTen(),
                    n.getCmnd(),
                    n.getSoDienThoai(),
                    n.getEmail() != null ? n.getEmail() : "",
                    n.getId()
            });
        }
    }

    private void themKhach() {
        String err = khachController.themKhach(
                tfTen.getText(),
                tfCmnd.getText(),
                tfSdt.getText(),
                tfEmail.getText()
        );
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Them khach thanh cong.");
            tfTen.setText("");
            tfCmnd.setText("");
            tfSdt.setText("");
            tfEmail.setText("");
            refresh();
        }
    }
}
