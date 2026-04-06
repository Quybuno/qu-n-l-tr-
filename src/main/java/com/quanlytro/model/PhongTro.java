package com.quanlytro.model;

import com.quanlytro.model.enums.TrangThaiPhong;

import java.math.BigDecimal;

public class PhongTro {

    private String id;
    /** FK day_tro — ma phong chi can unique trong cung mot day. */
    private String dayTroId;
    private String maPhong;
    private BigDecimal dienTich;
    private BigDecimal giaThueThang;
    private TrangThaiPhong trangThai;

    public PhongTro() {
    }

    public PhongTro(String id, String maPhong, BigDecimal dienTich, BigDecimal giaThueThang, TrangThaiPhong trangThai) {
        this.id = id;
        this.maPhong = maPhong;
        this.dienTich = dienTich;
        this.giaThueThang = giaThueThang;
        this.trangThai = trangThai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayTroId() {
        return dayTroId;
    }

    public void setDayTroId(String dayTroId) {
        this.dayTroId = dayTroId;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public BigDecimal getDienTich() {
        return dienTich;
    }

    public void setDienTich(BigDecimal dienTich) {
        this.dienTich = dienTich;
    }

    public BigDecimal getGiaThueThang() {
        return giaThueThang;
    }

    public void setGiaThueThang(BigDecimal giaThueThang) {
        this.giaThueThang = giaThueThang;
    }

    public TrangThaiPhong getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiPhong trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "PhongTro{"
                + "id='" + id + '\''
                + ", maPhong='" + maPhong + '\''
                + ", dienTich=" + dienTich
                + ", giaThueThang=" + giaThueThang
                + ", trangThai=" + trangThai
                + '}';
    }
}
