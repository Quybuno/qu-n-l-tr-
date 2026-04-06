-- MySQL / MariaDB — chay mot lan de tao database va bang

CREATE DATABASE IF NOT EXISTS quanly_phong_tro
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE quanly_phong_tro;

CREATE TABLE IF NOT EXISTS day_tro (
    id VARCHAR(36) PRIMARY KEY,
    ma_day VARCHAR(32) NOT NULL,
    ten_day VARCHAR(128) NOT NULL,
    dia_chi VARCHAR(512),
    UNIQUE KEY uk_ma_day (ma_day)
);

CREATE TABLE IF NOT EXISTS phong_tro (
    id VARCHAR(36) PRIMARY KEY,
    day_tro_id VARCHAR(36) NOT NULL,
    ma_phong VARCHAR(64) NOT NULL,
    dien_tich DECIMAL(12, 2),
    gia_thue_thang DECIMAL(15, 2) NOT NULL,
    trang_thai VARCHAR(32) NOT NULL,
    UNIQUE KEY uk_day_ma_phong (day_tro_id, ma_phong),
    CONSTRAINT fk_pt_day FOREIGN KEY (day_tro_id) REFERENCES day_tro (id)
);

CREATE TABLE IF NOT EXISTS nguoi_thue (
    id VARCHAR(36) PRIMARY KEY,
    ho_ten VARCHAR(128) NOT NULL,
    cmnd VARCHAR(20) NOT NULL,
    so_dien_thoai VARCHAR(20) NOT NULL,
    email VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS hop_dong (
    id VARCHAR(36) PRIMARY KEY,
    ma_hop_dong VARCHAR(64) NOT NULL,
    phong_tro_id VARCHAR(36) NOT NULL,
    nguoi_thue_id VARCHAR(36) NOT NULL,
    ngay_bat_dau DATE NOT NULL,
    ngay_ket_thuc DATE,
    tien_coc DECIMAL(15, 2) DEFAULT 0,
    gia_dien_moi_so DECIMAL(15, 4) NOT NULL DEFAULT 3500,
    gia_nuoc_moi_khoi DECIMAL(15, 4) NOT NULL DEFAULT 18000,
    CONSTRAINT fk_hd_phong FOREIGN KEY (phong_tro_id) REFERENCES phong_tro (id),
    CONSTRAINT fk_hd_nguoi FOREIGN KEY (nguoi_thue_id) REFERENCES nguoi_thue (id)
);

-- Chi so dien/nuoc theo ky (nam + thang) cho tung hop dong
CREATE TABLE IF NOT EXISTS chi_so_dien_nuoc (
    id VARCHAR(36) PRIMARY KEY,
    hop_dong_id VARCHAR(36) NOT NULL,
    nam INT NOT NULL,
    thang INT NOT NULL,
    chi_so_dien_cu DECIMAL(14, 4) NOT NULL,
    chi_so_dien_moi DECIMAL(14, 4) NOT NULL,
    chi_so_nuoc_cu DECIMAL(14, 4) NOT NULL,
    chi_so_nuoc_moi DECIMAL(14, 4) NOT NULL,
    UNIQUE KEY uk_cs_ky (hop_dong_id, nam, thang),
    CONSTRAINT fk_cs_hd FOREIGN KEY (hop_dong_id) REFERENCES hop_dong (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS hoa_don (
    id VARCHAR(36) PRIMARY KEY,
    ma_hoa_don VARCHAR(64) NOT NULL,
    hop_dong_id VARCHAR(36) NOT NULL,
    nam INT NOT NULL,
    thang INT NOT NULL,
    tien_phong DECIMAL(15, 2) NOT NULL,
    tien_dien DECIMAL(15, 2) NOT NULL,
    tien_nuoc DECIMAL(15, 2) NOT NULL,
    tong_tien DECIMAL(15, 2) NOT NULL,
    trang_thai VARCHAR(32) NOT NULL,
    UNIQUE KEY uk_hd_ky (hop_dong_id, nam, thang),
    CONSTRAINT fk_hd_hop_dong FOREIGN KEY (hop_dong_id) REFERENCES hop_dong (id)
);
