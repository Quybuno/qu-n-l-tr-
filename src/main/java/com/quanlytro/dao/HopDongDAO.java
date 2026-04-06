package com.quanlytro.dao;

import com.quanlytro.model.HopDong;
import com.quanlytro.utils.AppConstant;
import com.quanlytro.utils.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO implements IGenericDAO<HopDong> {

    private final PhongTroDAO phongTroDAO;
    private final NguoiThueDAO nguoiThueDAO;

    public HopDongDAO(PhongTroDAO phongTroDAO, NguoiThueDAO nguoiThueDAO) {
        this.phongTroDAO = phongTroDAO;
        this.nguoiThueDAO = nguoiThueDAO;
    }

    private static BigDecimal nz(BigDecimal v, BigDecimal def) {
        return v != null ? v : def;
    }

    private static HopDong mapRow(ResultSet rs) throws SQLException {
        HopDong h = new HopDong();
        h.setId(rs.getString("id"));
        h.setMaHopDong(rs.getString("ma_hop_dong"));
        h.setPhongTroId(rs.getString("phong_tro_id"));
        h.setNguoiThueId(rs.getString("nguoi_thue_id"));
        Date nb = rs.getDate("ngay_bat_dau");
        h.setNgayBatDau(nb != null ? nb.toLocalDate() : null);
        Date nk = rs.getDate("ngay_ket_thuc");
        h.setNgayKetThuc(nk != null ? nk.toLocalDate() : null);
        h.setTienCoc(rs.getBigDecimal("tien_coc"));
        h.setGiaDienMoiSo(nz(rs.getBigDecimal("gia_dien_moi_so"), AppConstant.DEFAULT_GIA_DIEN_MOI_SO));
        h.setGiaNuocMoiKhoi(nz(rs.getBigDecimal("gia_nuoc_moi_khoi"), AppConstant.DEFAULT_GIA_NUOC_MOI_KHOI));
        return h;
    }

    private HopDong hydrate(HopDong h) {
        if (h == null) {
            return null;
        }
        if (h.getPhongTroId() != null) {
            h.setPhongTro(phongTroDAO.findById(h.getPhongTroId()));
        }
        if (h.getNguoiThueId() != null) {
            h.setNguoiThue(nguoiThueDAO.findById(h.getNguoiThueId()));
        }
        return h;
    }

    @Override
    public void add(HopDong t) {
        String sql = """
                INSERT INTO hop_dong (id, ma_hop_dong, phong_tro_id, nguoi_thue_id, ngay_bat_dau, ngay_ket_thuc, tien_coc, gia_dien_moi_so, gia_nuoc_moi_khoi)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getMaHopDong());
            ps.setString(3, t.getPhongTroId());
            ps.setString(4, t.getNguoiThueId());
            ps.setDate(5, Date.valueOf(t.getNgayBatDau()));
            if (t.getNgayKetThuc() != null) {
                ps.setDate(6, Date.valueOf(t.getNgayKetThuc()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.setBigDecimal(7, t.getTienCoc() != null ? t.getTienCoc() : BigDecimal.ZERO);
            ps.setBigDecimal(8, t.getGiaDienMoiSo() != null ? t.getGiaDienMoiSo() : AppConstant.DEFAULT_GIA_DIEN_MOI_SO);
            ps.setBigDecimal(9, t.getGiaNuocMoiKhoi() != null ? t.getGiaNuocMoiKhoi() : AppConstant.DEFAULT_GIA_NUOC_MOI_KHOI);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(HopDong t) {
        String sql = """
                UPDATE hop_dong SET ma_hop_dong = ?, phong_tro_id = ?, nguoi_thue_id = ?, ngay_bat_dau = ?, ngay_ket_thuc = ?, tien_coc = ?, gia_dien_moi_so = ?, gia_nuoc_moi_khoi = ?
                WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getMaHopDong());
            ps.setString(2, t.getPhongTroId());
            ps.setString(3, t.getNguoiThueId());
            ps.setDate(4, Date.valueOf(t.getNgayBatDau()));
            if (t.getNgayKetThuc() != null) {
                ps.setDate(5, Date.valueOf(t.getNgayKetThuc()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
            ps.setBigDecimal(6, t.getTienCoc() != null ? t.getTienCoc() : BigDecimal.ZERO);
            ps.setBigDecimal(7, t.getGiaDienMoiSo() != null ? t.getGiaDienMoiSo() : AppConstant.DEFAULT_GIA_DIEN_MOI_SO);
            ps.setBigDecimal(8, t.getGiaNuocMoiKhoi() != null ? t.getGiaNuocMoiKhoi() : AppConstant.DEFAULT_GIA_NUOC_MOI_KHOI);
            ps.setString(9, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Object id) {
        String sql = "DELETE FROM hop_dong WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HopDong findById(Object id) {
        String sql = """
                SELECT id, ma_hop_dong, phong_tro_id, nguoi_thue_id, ngay_bat_dau, ngay_ket_thuc, tien_coc, gia_dien_moi_so, gia_nuoc_moi_khoi
                FROM hop_dong WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hydrate(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<HopDong> getAll() {
        String sql = """
                SELECT id, ma_hop_dong, phong_tro_id, nguoi_thue_id, ngay_bat_dau, ngay_ket_thuc, tien_coc, gia_dien_moi_so, gia_nuoc_moi_khoi
                FROM hop_dong ORDER BY ngay_bat_dau DESC
                """;
        List<HopDong> list = new ArrayList<>();
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(hydrate(mapRow(rs)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /** Khong hydrate — dung cho dieu kien nghiep vu (tranh vong lap). */
    public List<HopDong> getAllRaw() {
        String sql = """
                SELECT id, ma_hop_dong, phong_tro_id, nguoi_thue_id, ngay_bat_dau, ngay_ket_thuc, tien_coc, gia_dien_moi_so, gia_nuoc_moi_khoi
                FROM hop_dong ORDER BY ngay_bat_dau DESC
                """;
        List<HopDong> list = new ArrayList<>();
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
