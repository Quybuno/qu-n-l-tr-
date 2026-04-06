package com.quanlytro.controller;

import com.quanlytro.dao.ChiSoDienNuocDAO;
import com.quanlytro.dao.HoaDonDAO;
import com.quanlytro.dao.HopDongDAO;
import com.quanlytro.model.ChiSoDienNuoc;
import com.quanlytro.model.HoaDon;
import com.quanlytro.model.HopDong;
import com.quanlytro.model.enums.TrangThaiHoaDon;
import com.quanlytro.utils.AppConstant;
import com.quanlytro.utils.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HoaDonController {

    private final HoaDonDAO hoaDonDAO;
    private final HopDongDAO hopDongDAO;
    private final ChiSoDienNuocDAO chiSoDienNuocDAO;

    public HoaDonController(HoaDonDAO hoaDonDAO, HopDongDAO hopDongDAO, ChiSoDienNuocDAO chiSoDienNuocDAO) {
        this.hoaDonDAO = hoaDonDAO;
        this.hopDongDAO = hopDongDAO;
        this.chiSoDienNuocDAO = chiSoDienNuocDAO;
    }

    /** Tien phong = gia thang cua phong trong hop dong. */
    public BigDecimal layTienPhongTheoHopDong(String hopDongId) {
        HopDong hd = hopDongDAO.findById(hopDongId);
        if (hd == null || hd.getPhongTro() == null || hd.getPhongTro().getGiaThueThang() == null) {
            return null;
        }
        return hd.getPhongTro().getGiaThueThang();
    }

    /**
     * Tinh tien dien/nuoc tu chi so ky va don gia tren hop dong.
     *
     * @return [tienDien, tienNuoc] hoac null neu chua co chi so hoac so lieu khong hop le
     */
    public BigDecimal[] tinhTienDienNuocTuChiSo(String hopDongId, int nam, int thang) {
        ChiSoDienNuoc cs = chiSoDienNuocDAO.findByHopDongKy(hopDongId, nam, thang);
        HopDong hd = hopDongDAO.findById(hopDongId);
        if (cs == null || hd == null) {
            return null;
        }
        BigDecimal sd = cs.getChiSoDienMoi().subtract(cs.getChiSoDienCu());
        BigDecimal sn = cs.getChiSoNuocMoi().subtract(cs.getChiSoNuocCu());
        if (sd.signum() < 0 || sn.signum() < 0) {
            return null;
        }
        BigDecimal gd = hd.getGiaDienMoiSo() != null ? hd.getGiaDienMoiSo() : AppConstant.DEFAULT_GIA_DIEN_MOI_SO;
        BigDecimal gn = hd.getGiaNuocMoiKhoi() != null ? hd.getGiaNuocMoiKhoi() : AppConstant.DEFAULT_GIA_NUOC_MOI_KHOI;
        return new BigDecimal[]{sd.multiply(gd), sn.multiply(gn)};
    }

    public String taoHoaDon(HoaDon hoaDon) {
        if (!ValidationUtils.isNonBlank(hoaDon.getMaHoaDon())) {
            return "Ma hoa don khong duoc de trong.";
        }
        HopDong hd = hopDongDAO.findById(hoaDon.getHopDongId());
        if (hd == null) {
            return "Khong tim thay hop dong.";
        }
        if (hoaDon.getThang() < 1 || hoaDon.getThang() > 12) {
            return "Thang khong hop le.";
        }
        boolean trungKy = hoaDonDAO.getAll().stream().anyMatch(h ->
                Objects.equals(h.getHopDongId(), hoaDon.getHopDongId())
                        && h.getNam() == hoaDon.getNam()
                        && h.getThang() == hoaDon.getThang());
        if (trungKy) {
            return "Da co hoa don cho ky nay.";
        }
        BigDecimal tp = hoaDon.getTienPhong() != null ? hoaDon.getTienPhong() : BigDecimal.ZERO;
        BigDecimal td = hoaDon.getTienDien() != null ? hoaDon.getTienDien() : BigDecimal.ZERO;
        BigDecimal tn = hoaDon.getTienNuoc() != null ? hoaDon.getTienNuoc() : BigDecimal.ZERO;
        hoaDon.setTienPhong(tp);
        hoaDon.setTienDien(td);
        hoaDon.setTienNuoc(tn);
        BigDecimal tong = tp.add(td).add(tn);
        hoaDon.setTongTien(tong);
        hoaDon.setId(UUID.randomUUID().toString());
        if (hoaDon.getTrangThai() == null) {
            hoaDon.setTrangThai(TrangThaiHoaDon.CHUA_THANH_TOAN);
        }
        hoaDonDAO.add(hoaDon);
        return null;
    }

    public String danhDauDaThanhToan(String hoaDonId) {
        HoaDon h = hoaDonDAO.findById(hoaDonId);
        if (h == null) {
            return "Khong tim thay hoa don.";
        }
        h.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDonDAO.update(h);
        return null;
    }

    public List<HoaDon> danhSachHoaDon() {
        return hoaDonDAO.getAll();
    }
}
