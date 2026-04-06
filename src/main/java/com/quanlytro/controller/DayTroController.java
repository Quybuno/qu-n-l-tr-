package com.quanlytro.controller;

import com.quanlytro.dao.DayTroDAO;
import com.quanlytro.dao.PhongTroDAO;
import com.quanlytro.model.DayTro;
import com.quanlytro.utils.ValidationUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DayTroController {

    private final DayTroDAO dayTroDAO;
    private final PhongTroDAO phongTroDAO;

    public DayTroController(DayTroDAO dayTroDAO, PhongTroDAO phongTroDAO) {
        this.dayTroDAO = dayTroDAO;
        this.phongTroDAO = phongTroDAO;
    }

    public List<DayTro> danhSachDayTro() {
        return dayTroDAO.getAll();
    }

    public DayTro timDayTroTheoId(String id) {
        return dayTroDAO.findById(id);
    }

    public String themDayTro(String maDay, String tenDay, String diaChi) {
        if (!ValidationUtils.isNonBlank(maDay)) {
            return "Ma day tro khong duoc de trong.";
        }
        if (!ValidationUtils.isNonBlank(tenDay)) {
            return "Ten day tro khong duoc de trong.";
        }
        String ma = maDay.trim();
        boolean trungMa = dayTroDAO.getAll().stream()
                .anyMatch(d -> Objects.equals(d.getMaDay(), ma));
        if (trungMa) {
            return "Ma day tro da ton tai.";
        }
        DayTro d = new DayTro();
        d.setId(UUID.randomUUID().toString());
        d.setMaDay(ma);
        d.setTenDay(tenDay.trim());
        d.setDiaChi(diaChi != null && !diaChi.trim().isEmpty() ? diaChi.trim() : null);
        dayTroDAO.add(d);
        return null;
    }

    public String capNhatDayTro(String id, String maDay, String tenDay, String diaChi) {
        DayTro d = dayTroDAO.findById(id);
        if (d == null) {
            return "Khong tim thay day tro.";
        }
        if (!ValidationUtils.isNonBlank(maDay)) {
            return "Ma day tro khong duoc de trong.";
        }
        if (!ValidationUtils.isNonBlank(tenDay)) {
            return "Ten day tro khong duoc de trong.";
        }
        String ma = maDay.trim();
        boolean trungMa = dayTroDAO.getAll().stream()
                .anyMatch(x -> Objects.equals(x.getMaDay(), ma) && !Objects.equals(x.getId(), id));
        if (trungMa) {
            return "Ma day tro da ton tai.";
        }
        d.setMaDay(ma);
        d.setTenDay(tenDay.trim());
        d.setDiaChi(diaChi != null && !diaChi.trim().isEmpty() ? diaChi.trim() : null);
        dayTroDAO.update(d);
        return null;
    }

    public String xoaDayTro(String id) {
        if (phongTroDAO.countByDayTroId(id) > 0) {
            return "Khong xoa duoc day co phong — xoa / chuyen phong truoc.";
        }
        dayTroDAO.delete(id);
        return null;
    }
}
