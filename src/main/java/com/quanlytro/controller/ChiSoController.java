package com.quanlytro.controller;

import com.quanlytro.dao.ChiSoDienNuocDAO;
import com.quanlytro.dao.HopDongDAO;
import com.quanlytro.model.ChiSoDienNuoc;
import com.quanlytro.model.HopDong;

import java.util.List;
import java.util.UUID;

public class ChiSoController {

    private final ChiSoDienNuocDAO chiSoDienNuocDAO;
    private final HopDongDAO hopDongDAO;

    public ChiSoController(ChiSoDienNuocDAO chiSoDienNuocDAO, HopDongDAO hopDongDAO) {
        this.chiSoDienNuocDAO = chiSoDienNuocDAO;
        this.hopDongDAO = hopDongDAO;
    }

    /**
     * @return null neu thanh cong
     */
    public String luuChiSo(ChiSoDienNuoc c) {
        HopDong hd = hopDongDAO.findById(c.getHopDongId());
        if (hd == null) {
            return "Khong tim thay hop dong.";
        }
        if (c.getThang() < 1 || c.getThang() > 12) {
            return "Thang khong hop le.";
        }
        if (c.getChiSoDienMoi().compareTo(c.getChiSoDienCu()) < 0) {
            return "Chi so dien moi phai >= chi so cu.";
        }
        if (c.getChiSoNuocMoi().compareTo(c.getChiSoNuocCu()) < 0) {
            return "Chi so nuoc moi phai >= chi so cu.";
        }
        ChiSoDienNuoc existing = chiSoDienNuocDAO.findByHopDongKy(c.getHopDongId(), c.getNam(), c.getThang());
        if (existing != null) {
            c.setId(existing.getId());
            chiSoDienNuocDAO.update(c);
        } else {
            c.setId(UUID.randomUUID().toString());
            chiSoDienNuocDAO.add(c);
        }
        return null;
    }

    public List<ChiSoDienNuoc> danhSachTheoHopDong(String hopDongId) {
        return chiSoDienNuocDAO.findByHopDong(hopDongId);
    }

    public List<ChiSoDienNuoc> danhSachTatCa() {
        return chiSoDienNuocDAO.findAll();
    }

    public HopDong timHopDong(String id) {
        return hopDongDAO.findById(id);
    }
}
