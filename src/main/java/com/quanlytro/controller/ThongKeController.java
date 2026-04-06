package com.quanlytro.controller;

import com.quanlytro.context.DayTroContext;
import com.quanlytro.dao.HoaDonDAO;
import com.quanlytro.dao.PhongTroDAO;
import com.quanlytro.model.HoaDon;
import com.quanlytro.model.HopDong;
import com.quanlytro.model.PhongTro;
import com.quanlytro.model.enums.TrangThaiHoaDon;
import com.quanlytro.model.enums.TrangThaiPhong;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ThongKeController {

    private final PhongTroDAO phongTroDAO;
    private final HoaDonDAO hoaDonDAO;

    public ThongKeController(PhongTroDAO phongTroDAO, HoaDonDAO hoaDonDAO) {
        this.phongTroDAO = phongTroDAO;
        this.hoaDonDAO = hoaDonDAO;
    }

    private static boolean hoaDonThuocDay(HoaDon h, String dayTroId) {
        HopDong hd = h.getHopDong();
        if (hd == null) {
            return false;
        }
        PhongTro pt = hd.getPhongTro();
        if (pt == null) {
            return false;
        }
        return Objects.equals(pt.getDayTroId(), dayTroId);
    }

    public long demPhongTrong() {
        String dayId = DayTroContext.getSelectedDayTroId();
        if (dayId == null) {
            return 0;
        }
        return phongTroDAO.getByDayTroId(dayId).stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .count();
    }

    public List<PhongTro> phongDangTrong() {
        String dayId = DayTroContext.getSelectedDayTroId();
        if (dayId == null) {
            return List.of();
        }
        return phongTroDAO.getByDayTroId(dayId).stream()
                .filter(p -> p.getTrangThai() == TrangThaiPhong.TRONG)
                .collect(Collectors.toList());
    }

    public BigDecimal tongDoanhThuThang(int nam, int thang) {
        String dayId = DayTroContext.getSelectedDayTroId();
        if (dayId == null) {
            return BigDecimal.ZERO;
        }
        return hoaDonDAO.getAll().stream()
                .filter(h -> h.getNam() == nam && h.getThang() == thang)
                .filter(h -> h.getTrangThai() == TrangThaiHoaDon.DA_THANH_TOAN)
                .filter(h -> hoaDonThuocDay(h, dayId))
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<HoaDon> hoaDonChuaThanhToan() {
        String dayId = DayTroContext.getSelectedDayTroId();
        if (dayId == null) {
            return List.of();
        }
        return hoaDonDAO.getAll().stream()
                .filter(h -> h.getTrangThai() == TrangThaiHoaDon.CHUA_THANH_TOAN)
                .filter(h -> hoaDonThuocDay(h, dayId))
                .collect(Collectors.toList());
    }

    /** Hoa don da TT trong ky — da hydrate hop dong / phong qua DAO. */
    public List<HoaDon> hoaDonDaThanhToanTheoKy(int nam, int thang) {
        String dayId = DayTroContext.getSelectedDayTroId();
        if (dayId == null) {
            return List.of();
        }
        return hoaDonDAO.getAll().stream()
                .filter(h -> h.getNam() == nam && h.getThang() == thang)
                .filter(h -> h.getTrangThai() == TrangThaiHoaDon.DA_THANH_TOAN)
                .filter(h -> hoaDonThuocDay(h, dayId))
                .collect(Collectors.toList());
    }
}
