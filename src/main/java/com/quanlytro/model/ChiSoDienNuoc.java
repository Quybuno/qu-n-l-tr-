package com.quanlytro.model;

import java.math.BigDecimal;

/**
 * Chi so dien/nuoc theo ky (nam + thang) cho mot hop dong.
 */
public class ChiSoDienNuoc {

    private String id;
    private String hopDongId;
    private int nam;
    private int thang;
    private BigDecimal chiSoDienCu;
    private BigDecimal chiSoDienMoi;
    private BigDecimal chiSoNuocCu;
    private BigDecimal chiSoNuocMoi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getChiSoDienCu() {
        return chiSoDienCu;
    }

    public void setChiSoDienCu(BigDecimal chiSoDienCu) {
        this.chiSoDienCu = chiSoDienCu;
    }

    public BigDecimal getChiSoDienMoi() {
        return chiSoDienMoi;
    }

    public void setChiSoDienMoi(BigDecimal chiSoDienMoi) {
        this.chiSoDienMoi = chiSoDienMoi;
    }

    public BigDecimal getChiSoNuocCu() {
        return chiSoNuocCu;
    }

    public void setChiSoNuocCu(BigDecimal chiSoNuocCu) {
        this.chiSoNuocCu = chiSoNuocCu;
    }

    public BigDecimal getChiSoNuocMoi() {
        return chiSoNuocMoi;
    }

    public void setChiSoNuocMoi(BigDecimal chiSoNuocMoi) {
        this.chiSoNuocMoi = chiSoNuocMoi;
    }
}
