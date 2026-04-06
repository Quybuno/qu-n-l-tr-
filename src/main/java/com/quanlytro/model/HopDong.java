package com.quanlytro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Luu file theo phongTroId / nguoiThueId; tham chieu day du nap sau khi load (transient).
 */
public class HopDong {

    private String id;
    private String maHopDong;
    private String phongTroId;
    private String nguoiThueId;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal tienCoc;
    /** VND / 1 kWh */
    private BigDecimal giaDienMoiSo;
    /** VND / 1 m3 nuoc */
    private BigDecimal giaNuocMoiKhoi;

    private transient PhongTro phongTro;
    private transient NguoiThue nguoiThue;

    public HopDong() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(String maHopDong) {
        this.maHopDong = maHopDong;
    }

    public String getPhongTroId() {
        return phongTroId;
    }

    public void setPhongTroId(String phongTroId) {
        this.phongTroId = phongTroId;
    }

    public String getNguoiThueId() {
        return nguoiThueId;
    }

    public void setNguoiThueId(String nguoiThueId) {
        this.nguoiThueId = nguoiThueId;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public BigDecimal getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(BigDecimal tienCoc) {
        this.tienCoc = tienCoc;
    }

    public BigDecimal getGiaDienMoiSo() {
        return giaDienMoiSo;
    }

    public void setGiaDienMoiSo(BigDecimal giaDienMoiSo) {
        this.giaDienMoiSo = giaDienMoiSo;
    }

    public BigDecimal getGiaNuocMoiKhoi() {
        return giaNuocMoiKhoi;
    }

    public void setGiaNuocMoiKhoi(BigDecimal giaNuocMoiKhoi) {
        this.giaNuocMoiKhoi = giaNuocMoiKhoi;
    }

    public PhongTro getPhongTro() {
        return phongTro;
    }

    public void setPhongTro(PhongTro phongTro) {
        this.phongTro = phongTro;
    }

    public NguoiThue getNguoiThue() {
        return nguoiThue;
    }

    public void setNguoiThue(NguoiThue nguoiThue) {
        this.nguoiThue = nguoiThue;
    }

    @Override
    public String toString() {
        return "HopDong{"
                + "id='" + id + '\''
                + ", maHopDong='" + maHopDong + '\''
                + ", phongTroId='" + phongTroId + '\''
                + ", nguoiThueId='" + nguoiThueId + '\''
                + ", ngayBatDau=" + ngayBatDau
                + ", ngayKetThuc=" + ngayKetThuc
                + ", tienCoc=" + tienCoc
                + '}';
    }
}
