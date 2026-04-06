package com.quanlytro.dao;

import com.quanlytro.model.PhongTro;
import com.quanlytro.model.enums.TrangThaiPhong;
import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongTroDAO implements IGenericDAO<PhongTro> {

    private static PhongTro mapRow(ResultSet rs) throws SQLException {
        PhongTro p = new PhongTro();
        p.setId(rs.getString("id"));
        p.setDayTroId(rs.getString("day_tro_id"));
        p.setMaPhong(rs.getString("ma_phong"));
        p.setDienTich(rs.getBigDecimal("dien_tich"));
        p.setGiaThueThang(rs.getBigDecimal("gia_thue_thang"));
        p.setTrangThai(TrangThaiPhong.valueOf(rs.getString("trang_thai")));
        return p;
    }

    @Override
    public void add(PhongTro t) {
        String sql = """
                INSERT INTO phong_tro (id, day_tro_id, ma_phong, dien_tich, gia_thue_thang, trang_thai)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getDayTroId());
            ps.setString(3, t.getMaPhong());
            ps.setObject(4, t.getDienTich());
            ps.setBigDecimal(5, t.getGiaThueThang());
            ps.setString(6, t.getTrangThai().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PhongTro t) {
        String sql = """
                UPDATE phong_tro SET day_tro_id = ?, ma_phong = ?, dien_tich = ?, gia_thue_thang = ?, trang_thai = ?
                WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getDayTroId());
            ps.setString(2, t.getMaPhong());
            ps.setObject(3, t.getDienTich());
            ps.setBigDecimal(4, t.getGiaThueThang());
            ps.setString(5, t.getTrangThai().name());
            ps.setString(6, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Object id) {
        String sql = "DELETE FROM phong_tro WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PhongTro findById(Object id) {
        String sql = """
                SELECT id, day_tro_id, ma_phong, dien_tich, gia_thue_thang, trang_thai
                FROM phong_tro WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
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

    @Override
    public List<PhongTro> getAll() {
        String sql = """
                SELECT id, day_tro_id, ma_phong, dien_tich, gia_thue_thang, trang_thai
                FROM phong_tro ORDER BY day_tro_id, ma_phong
                """;
        List<PhongTro> list = new ArrayList<>();
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

    public List<PhongTro> getByDayTroId(String dayTroId) {
        String sql = """
                SELECT id, day_tro_id, ma_phong, dien_tich, gia_thue_thang, trang_thai
                FROM phong_tro WHERE day_tro_id = ? ORDER BY ma_phong
                """;
        List<PhongTro> list = new ArrayList<>();
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

    public long countByDayTroId(String dayTroId) {
        String sql = "SELECT COUNT(*) FROM phong_tro WHERE day_tro_id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dayTroId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
