package com.quanlytro.ui;

import com.quanlytro.controller.DayTroController;
import com.quanlytro.model.DayTro;

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

public class DayTroPanel extends JPanel implements Refreshable {

    private final DayTroController dayTroController;
    private final Runnable onDayListChanged;
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField tfMa = new JTextField(14);
    private final JTextField tfTen = new JTextField(24);
    private final JTextField tfDiaChi = new JTextField(32);

    public DayTroPanel(DayTroController dayTroController, Runnable onDayListChanged) {
        this.dayTroController = dayTroController;
        this.onDayListChanged = onDayListChanged;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] cols = {"Ma day", "Ten day", "Dia chi", "ID"};
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
        form.setBorder(BorderFactory.createTitledBorder("Them day tro"));
        form.add(new JLabel("Ma day (duy nhat):"));
        form.add(tfMa);
        form.add(new JLabel("Ten day:"));
        form.add(tfTen);
        form.add(new JLabel("Dia chi (tuy chon):"));
        form.add(tfDiaChi);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Them day tro");
        btnThem.addActionListener(e -> themDay());
        JButton btnSua = new JButton("Sua day (chon dong)");
        btnSua.addActionListener(e -> suaDay());
        JButton btnXoa = new JButton("Xoa day (chon dong)");
        btnXoa.addActionListener(e -> xoaDay());
        JButton btnTai = new JButton("Tai lai");
        btnTai.addActionListener(e -> refresh());
        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnTai);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JLabel hint = new JLabel(
                "<html><small>Phong, hop dong, thong ke theo <b>day dang chon</b> o tren cung cua cua so.</small></html>");
        add(hint, BorderLayout.SOUTH);

        refresh();
        UiUtils.refreshWhenPanelShown(this, this::refreshData);
    }

    @Override
    public void refreshData() {
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        for (DayTro d : dayTroController.danhSachDayTro()) {
            tableModel.addRow(new Object[]{
                    d.getMaDay(),
                    d.getTenDay(),
                    d.getDiaChi() != null ? d.getDiaChi() : "",
                    d.getId()
            });
        }
    }

    private void themDay() {
        String err = dayTroController.themDayTro(tfMa.getText(), tfTen.getText(), tfDiaChi.getText());
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Da them day tro.");
            tfMa.setText("");
            tfTen.setText("");
            tfDiaChi.setText("");
            refresh();
            onDayListChanged.run();
        }
    }

    private void suaDay() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtils.error(this, "Chon mot dong.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 3);
        DayTro d = dayTroController.timDayTroTheoId(id);
        if (d == null) {
            UiUtils.error(this, "Khong tim thay day.");
            return;
        }
        JTextField ma = new JTextField(d.getMaDay(), 12);
        JTextField ten = new JTextField(d.getTenDay(), 20);
        JTextField dc = new JTextField(d.getDiaChi() != null ? d.getDiaChi() : "", 28);
        JPanel p = new JPanel(new GridLayout(0, 1, 4, 4));
        p.add(new JLabel("Ma day:"));
        p.add(ma);
        p.add(new JLabel("Ten day:"));
        p.add(ten);
        p.add(new JLabel("Dia chi:"));
        p.add(dc);
        int ok = JOptionPane.showConfirmDialog(this, p, "Sua day tro", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) {
            return;
        }
        String err = dayTroController.capNhatDayTro(id, ma.getText(), ten.getText(), dc.getText());
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Da cap nhat.");
            refresh();
            onDayListChanged.run();
        }
    }

    private void xoaDay() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UiUtils.error(this, "Chon mot dong.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 3);
        int c = JOptionPane.showConfirmDialog(this, "Xoa day tro nay? (Chi duoc khi chua co phong.)",
                "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) {
            return;
        }
        String err = dayTroController.xoaDayTro(id);
        if (err != null) {
            UiUtils.error(this, err);
        } else {
            UiUtils.info(this, "Da xoa.");
            refresh();
            onDayListChanged.run();
        }
    }
}
