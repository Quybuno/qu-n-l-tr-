package com.quanlytro.controller;

import com.quanlytro.dao.HopDongDAO;
import com.quanlytro.dao.NguoiThueDAO;
import com.quanlytro.model.NguoiThue;
import com.quanlytro.utils.ValidationUtils;

import java.util.List;
import java.util.UUID;

public class KhachController {

    private final NguoiThueDAO nguoiThueDAO;
    private final HopDongDAO hopDongDAO;

    public KhachController(NguoiThueDAO nguoiThueDAO, HopDongDAO hopDongDAO) {
        this.nguoiThueDAO = nguoiThueDAO;
        this.hopDongDAO = hopDongDAO;
    }

    public String themKhach(String hoTen, String cmnd, String sdt, String email) {
        if (!ValidationUtils.isNonBlank(hoTen)) {
            return "Ho ten khong duoc de trong.";
        }
        if (!ValidationUtils.isCmnd(cmnd)) {
            return "CMND/CCCD phai co 9 hoac 12 chu so.";
        }
        if (!ValidationUtils.isPhone(sdt)) {
            return "So dien thoai khong hop le (VD: 09xxxxxxxx).";
        }
        NguoiThue n = new NguoiThue();
        n.setId(UUID.randomUUID().toString());
        n.setHoTen(hoTen.trim());
        n.setCmnd(cmnd.trim());
        n.setSoDienThoai(sdt.trim());
        n.setEmail(email != null ? email.trim() : "");
        nguoiThueDAO.add(n);
        return null;
    }


    public String xoaKhach(String nguoiThueId) {
        if (!ValidationUtils.isNonBlank(nguoiThueId)) {
            return "Khong tim thay ID khach.";
        }
        if (nguoiThueDAO.findById(nguoiThueId) == null) {
            return "Khach khong ton tai.";
        }
        if (hopDongDAO.existsByNguoiThueId(nguoiThueId)) {
            return "Khong the xoa: khach dang/da co hop dong.";
        }
        nguoiThueDAO.delete(nguoiThueId);
        return null;
    }

    public List<NguoiThue> danhSachKhach() {
        return nguoiThueDAO.getAll();
    }

    public NguoiThue timKhachTheoId(String id) {
        return nguoiThueDAO.findById(id);
    }
}
