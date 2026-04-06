package com.quanlytro.model;

public class NguoiThue {

    private String id;
    private String hoTen;
    private String cmnd;
    private String soDienThoai;
    private String email;

    public NguoiThue() {
    }

    public NguoiThue(String id, String hoTen, String cmnd, String soDienThoai, String email) {
        this.id = id;
        this.hoTen = hoTen;
        this.cmnd = cmnd;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NguoiThue{"
                + "id='" + id + '\''
                + ", hoTen='" + hoTen + '\''
                + ", cmnd='" + cmnd + '\''
                + ", soDienThoai='" + soDienThoai + '\''
                + ", email='" + email + '\''
                + '}';
    }
}
