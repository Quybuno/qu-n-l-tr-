package com.quanlytro.controller;

import com.quanlytro.dao.PhongTroDAO;
import com.quanlytro.model.PhongTro;
import com.quanlytro.model.enums.TrangThaiPhong;
import com.quanlytro.utils.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PhongController {

    private final PhongTroDAO phongTroDAO;

    public PhongController(PhongTroDAO phongTroDAO) {
        this.phongTroDAO = phongTroDAO;
    }

    public String themPhong(String maPhong, BigDecimal dienTich, BigDecimal giaThue) {
        if (!ValidationUtils.isNonBlank(maPhong)) {
            return "Ma phong khong duoc de trong.";
        }
        if (giaThue == null || giaThue.signum() <= 0) {
            return "Gia thue phai lon hon 0.";
        }
        boolean trungMa = phongTroDAO.getAll().stream()
                .anyMatch(p -> Objects.equals(p.getMaPhong(), maPhong.trim()));
        if (trungMa) {
            return "Ma phong da ton tai.";
        }
        PhongTro p = new PhongTro();
        p.setId(UUID.randomUUID().toString());
        p.setMaPhong(maPhong.trim());
        p.setDienTich(dienTich);
        p.setGiaThueThang(giaThue);
        p.setTrangThai(TrangThaiPhong.TRONG);
        phongTroDAO.add(p);
        return null;
    }

    public List<PhongTro> danhSachPhong() {
        return phongTroDAO.getAll();
    }

    public PhongTro timPhongTheoId(String id) {
        return phongTroDAO.findById(id);
    }

    public String capNhatGiaPhong(String id, BigDecimal giaMoi) {
        PhongTro p = phongTroDAO.findById(id);
        if (p == null) {
            return "Khong tim thay phong.";
        }
        if (giaMoi == null || giaMoi.signum() <= 0) {
            return "Gia thue phai lon hon 0.";
        }
        p.setGiaThueThang(giaMoi);
        phongTroDAO.update(p);
        return null;
    }
}
