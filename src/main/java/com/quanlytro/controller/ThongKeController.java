package com.quanlytro.controller;

import com.quanlytro.dao.HoaDonDAO;
import com.quanlytro.dao.PhongTroDAO;
import com.quanlytro.model.HoaDon;
import com.quanlytro.model.PhongTro;
import com.quanlytro.model.enums.TrangThaiHoaDon;
import com.quanlytro.model.enums.TrangThaiPhong;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ThongKeController {

    private final PhongTroDAO phongTroDAO;
    private final HoaDonDAO hoaDonDAO;

    public ThongKeController(PhongTroDAO phongTroDAO, HoaDonDAO hoaDonDAO) {
        this.phongTroDAO = phongTroDAO;
        this.hoaDonDAO = hoaDonDAO;
    }

    public long demPhongTrong() {
        return phongTroDAO.getAll().stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .count();
    }

    public List<PhongTro> phongDangTrong() {
        return phongTroDAO.getAll().stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .collect(Collectors.toList());
    }

    public BigDecimal tongDoanhThuThang(int nam, int thang) {
        return hoaDonDAO.getAll().stream()
                .filter(h -> h.getNam() == nam && h.getThang() == thang)
                .filter(h -> h.getTrangThai() == TrangThaiHoaDon.DA_THANH_TOAN)
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<HoaDon> hoaDonChuaThanhToan() {
        return hoaDonDAO.getAll().stream()
                .filter(h -> h.getTrangThai() == TrangThaiHoaDon.CHUA_THANH_TOAN)
                .collect(Collectors.toList());
    }

    /** Hoa don da TT trong ky — da hydrate hop dong / phong qua DAO. */
    public List<HoaDon> hoaDonDaThanhToanTheoKy(int nam, int thang) {
        return hoaDonDAO.getAll().stream()
                .filter(h -> h.getNam() == nam && h.getThang() == thang)
                .filter(h -> h.getTrangThai() == TrangThaiHoaDon.DA_THANH_TOAN)
                .collect(Collectors.toList());
    }
}
