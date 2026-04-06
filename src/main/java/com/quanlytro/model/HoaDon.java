package com.quanlytro.model;

import com.quanlytro.model.enums.TrangThaiHoaDon;

import java.math.BigDecimal;

public class HoaDon {

    private String id;
    private String maHoaDon;
    private String hopDongId;
    private int nam;
    private int thang;
    private BigDecimal tienPhong;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tongTien;
    private TrangThaiHoaDon trangThai;

    private transient HopDong hopDong;

    public HoaDon() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getHopDongId() {
        return hopDongId;
    }

    public void setHopDongId(String hopDongId) {
        this.hopDongId = hopDongId;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public BigDecimal getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(BigDecimal tienPhong) {
        this.tienPhong = tienPhong;
    }

    public BigDecimal getTienDien() {
        return tienDien;
    }

    public void setTienDien(BigDecimal tienDien) {
        this.tienDien = tienDien;
    }

    public BigDecimal getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(BigDecimal tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public TrangThaiHoaDon getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiHoaDon trangThai) {
        this.trangThai = trangThai;
    }

    public HopDong getHopDong() {
        return hopDong;
    }

    public void setHopDong(HopDong hopDong) {
        this.hopDong = hopDong;
    }

    @Override
    public String toString() {
        return "HoaDon{"
                + "id='" + id + '\''
                + ", maHoaDon='" + maHoaDon + '\''
                + ", hopDongId='" + hopDongId + '\''
                + ", nam=" + nam
                + ", thang=" + thang
                + ", tongTien=" + tongTien
                + ", trangThai=" + trangThai
                + '}';
    }
}
