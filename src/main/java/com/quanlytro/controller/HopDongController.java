package com.quanlytro.controller;

import com.quanlytro.dao.HopDongDAO;
import com.quanlytro.dao.NguoiThueDAO;
import com.quanlytro.dao.PhongTroDAO;
import com.quanlytro.model.HopDong;
import com.quanlytro.model.PhongTro;
import com.quanlytro.model.enums.TrangThaiPhong;
import com.quanlytro.utils.AppConstant;
import com.quanlytro.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HopDongController {

    private final PhongTroDAO phongTroDAO;
    private final NguoiThueDAO nguoiThueDAO;
    private final HopDongDAO hopDongDAO;

    public HopDongController(PhongTroDAO phongTroDAO, NguoiThueDAO nguoiThueDAO, HopDongDAO hopDongDAO) {
        this.phongTroDAO = phongTroDAO;
        this.nguoiThueDAO = nguoiThueDAO;
        this.hopDongDAO = hopDongDAO;
    }

    /**
     * @return null neu thanh cong, chuoi loi neu that bai
     */
    public String taoHopDong(HopDong hopDong) {
        if (!ValidationUtils.isNonBlank(hopDong.getMaHopDong())) {
            return "Ma hop dong khong duoc de trong.";
        }
        PhongTro phong = phongTroDAO.findById(hopDong.getPhongTroId());
        if (phong == null) {
            return "Khong tim thay phong.";
        }
        if (phong.getTrangThai() != TrangThaiPhong.TRONG) {
            return "Phong khong con trong.";
        }
        if (nguoiThueDAO.findById(hopDong.getNguoiThueId()) == null) {
            return "Khong tim thay nguoi thue.";
        }
        if (hopDong.getNgayBatDau() == null) {
            return "Ngay bat dau khong hop le.";
        }
        if (nguoiCoHopDongHieuLucKhac(hopDong.getNguoiThueId(), null)) {
            return "Nguoi thue dang co hop dong hieu luc khac.";
        }
        if (hopDong.getGiaDienMoiSo() == null) {
            hopDong.setGiaDienMoiSo(AppConstant.DEFAULT_GIA_DIEN_MOI_SO);
        }
        if (hopDong.getGiaNuocMoiKhoi() == null) {
            hopDong.setGiaNuocMoiKhoi(AppConstant.DEFAULT_GIA_NUOC_MOI_KHOI);
        }
        hopDong.setId(UUID.randomUUID().toString());
        hopDongDAO.add(hopDong);
        phong.setTrangThai(TrangThaiPhong.DA_THUE);
        phongTroDAO.update(phong);
        return null;
    }

    private boolean nguoiCoHopDongHieuLucKhac(String nguoiThueId, String boQuaHopDongId) {
        LocalDate today = LocalDate.now();
        for (HopDong h : hopDongDAO.getAllRaw()) {
            if (!Objects.equals(h.getNguoiThueId(), nguoiThueId)) {
                continue;
            }
            if (boQuaHopDongId != null && Objects.equals(h.getId(), boQuaHopDongId)) {
                continue;
            }
            if (hopDongHieuLucTai(h, today)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hopDongHieuLucTai(HopDong h, LocalDate today) {
        if (h.getNgayBatDau() == null || h.getNgayBatDau().isAfter(today)) {
            return false;
        }
        if (h.getNgayKetThuc() == null) {
            return true;
        }
        return !h.getNgayKetThuc().isBefore(today);
    }

    public List<HopDong> danhSachHopDong() {
        return hopDongDAO.getAll();
    }

    public HopDong timHopDongTheoId(String id) {
        return hopDongDAO.findById(id);
    }
}
