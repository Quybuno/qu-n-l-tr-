package com.quanlytro.dao;

import com.quanlytro.model.ChiSoDienNuoc;
import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiSoDienNuocDAO {

    private static ChiSoDienNuoc mapRow(ResultSet rs) throws SQLException {
        ChiSoDienNuoc c = new ChiSoDienNuoc();
        c.setId(rs.getString("id"));
        c.setHopDongId(rs.getString("hop_dong_id"));
        c.setNam(rs.getInt("nam"));
        c.setThang(rs.getInt("thang"));
        c.setChiSoDienCu(rs.getBigDecimal("chi_so_dien_cu"));
        c.setChiSoDienMoi(rs.getBigDecimal("chi_so_dien_moi"));
        c.setChiSoNuocCu(rs.getBigDecimal("chi_so_nuoc_cu"));
        c.setChiSoNuocMoi(rs.getBigDecimal("chi_so_nuoc_moi"));
        return c;
    }

    public void add(ChiSoDienNuoc t) {
        String sql = """
                INSERT INTO chi_so_dien_nuoc (id, hop_dong_id, nam, thang, chi_so_dien_cu, chi_so_dien_moi, chi_so_nuoc_cu, chi_so_nuoc_moi)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getHopDongId());
            ps.setInt(3, t.getNam());
            ps.setInt(4, t.getThang());
            ps.setBigDecimal(5, t.getChiSoDienCu());
            ps.setBigDecimal(6, t.getChiSoDienMoi());
            ps.setBigDecimal(7, t.getChiSoNuocCu());
            ps.setBigDecimal(8, t.getChiSoNuocMoi());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(ChiSoDienNuoc t) {
        String sql = """
                UPDATE chi_so_dien_nuoc SET hop_dong_id = ?, nam = ?, thang = ?, chi_so_dien_cu = ?, chi_so_dien_moi = ?, chi_so_nuoc_cu = ?, chi_so_nuoc_moi = ?
                WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getHopDongId());
            ps.setInt(2, t.getNam());
            ps.setInt(3, t.getThang());
            ps.setBigDecimal(4, t.getChiSoDienCu());
            ps.setBigDecimal(5, t.getChiSoDienMoi());
            ps.setBigDecimal(6, t.getChiSoNuocCu());
            ps.setBigDecimal(7, t.getChiSoNuocMoi());
            ps.setString(8, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ChiSoDienNuoc findByHopDongKy(String hopDongId, int nam, int thang) {
        String sql = """
                SELECT id, hop_dong_id, nam, thang, chi_so_dien_cu, chi_so_dien_moi, chi_so_nuoc_cu, chi_so_nuoc_moi
                FROM chi_so_dien_nuoc WHERE hop_dong_id = ? AND nam = ? AND thang = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hopDongId);
            ps.setInt(2, nam);
            ps.setInt(3, thang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<ChiSoDienNuoc> findByHopDong(String hopDongId) {
        String sql = """
                SELECT id, hop_dong_id, nam, thang, chi_so_dien_cu, chi_so_dien_moi, chi_so_nuoc_cu, chi_so_nuoc_moi
                FROM chi_so_dien_nuoc WHERE hop_dong_id = ? ORDER BY nam DESC, thang DESC
                """;
        List<ChiSoDienNuoc> list = new ArrayList<>();
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hopDongId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<ChiSoDienNuoc> findAll() {
        String sql = """
                SELECT id, hop_dong_id, nam, thang, chi_so_dien_cu, chi_so_dien_moi, chi_so_nuoc_cu, chi_so_nuoc_moi
                FROM chi_so_dien_nuoc ORDER BY nam DESC, thang DESC, hop_dong_id
                """;
        List<ChiSoDienNuoc> list = new ArrayList<>();
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

    public List<ChiSoDienNuoc> findAllByDayTroId(String dayTroId) {
        String sql = """
                SELECT c.id, c.hop_dong_id, c.nam, c.thang, c.chi_so_dien_cu, c.chi_so_dien_moi, c.chi_so_nuoc_cu, c.chi_so_nuoc_moi
                FROM chi_so_dien_nuoc c
                INNER JOIN hop_dong h ON c.hop_dong_id = h.id
                INNER JOIN phong_tro p ON h.phong_tro_id = p.id
                WHERE p.day_tro_id = ?
                ORDER BY c.nam DESC, c.thang DESC, c.hop_dong_id
                """;
        List<ChiSoDienNuoc> list = new ArrayList<>();
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dayTroId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
