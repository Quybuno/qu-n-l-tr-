package com.quanlytro;

import com.quanlytro.controller.ChiSoController;
import com.quanlytro.controller.DayTroController;
import com.quanlytro.controller.HoaDonController;
import com.quanlytro.controller.HopDongController;
import com.quanlytro.controller.KhachController;
import com.quanlytro.controller.PhongController;
import com.quanlytro.controller.ThongKeController;
import com.quanlytro.dao.ChiSoDienNuocDAO;
import com.quanlytro.dao.DayTroDAO;
import com.quanlytro.dao.HoaDonDAO;
import com.quanlytro.dao.HopDongDAO;
import com.quanlytro.dao.NguoiThueDAO;
import com.quanlytro.dao.PhongTroDAO;
import com.quanlytro.db.SchemaMigrator;
import com.quanlytro.ui.MainFrame;

import javax.swing.SwingUtilities;

public final class Main {

    public static void main(String[] args) {
        SchemaMigrator.ensureLatest();

        PhongTroDAO phongTroDAO = new PhongTroDAO();
        DayTroDAO dayTroDAO = new DayTroDAO();
        NguoiThueDAO nguoiThueDAO = new NguoiThueDAO();
        HopDongDAO hopDongDAO = new HopDongDAO(phongTroDAO, nguoiThueDAO);
        ChiSoDienNuocDAO chiSoDienNuocDAO = new ChiSoDienNuocDAO();
        HoaDonDAO hoaDonDAO = new HoaDonDAO(hopDongDAO);

        PhongController phongController = new PhongController(phongTroDAO);
        DayTroController dayTroController = new DayTroController(dayTroDAO, phongTroDAO);
        KhachController khachController = new KhachController(nguoiThueDAO);
        HopDongController hopDongController = new HopDongController(phongTroDAO, nguoiThueDAO, hopDongDAO);
        ChiSoController chiSoController = new ChiSoController(chiSoDienNuocDAO, hopDongDAO);
        HoaDonController hoaDonController = new HoaDonController(hoaDonDAO, hopDongDAO, chiSoDienNuocDAO);
        ThongKeController thongKeController = new ThongKeController(phongTroDAO, hoaDonDAO);

        SwingUtilities.invokeLater(() -> {
            MainFrame.applyLookAndFeel();
            MainFrame frame = new MainFrame(
                    phongController,
                    khachController,
                    hopDongController,
                    chiSoController,
                    hoaDonController,
                    thongKeController,
                    dayTroController
            );
            frame.setVisible(true);
        });
    }

    private Main() {
    }
}
