-- Chay neu ban da co database cu (chua co cot don gia va bang chi so)

USE quanly_phong_tro;

ALTER TABLE hop_dong
    ADD COLUMN gia_dien_moi_so DECIMAL(15, 4) NOT NULL DEFAULT 3500 AFTER tien_coc,
    ADD COLUMN gia_nuoc_moi_khoi DECIMAL(15, 4) NOT NULL DEFAULT 18000 AFTER gia_dien_moi_so;

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
