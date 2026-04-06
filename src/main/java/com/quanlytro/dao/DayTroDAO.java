package com.quanlytro.dao;

import com.quanlytro.model.DayTro;
import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DayTroDAO implements IGenericDAO<DayTro> {

    private static DayTro mapRow(ResultSet rs) throws SQLException {
        DayTro d = new DayTro();
        d.setId(rs.getString("id"));
        d.setMaDay(rs.getString("ma_day"));
        d.setTenDay(rs.getString("ten_day"));
        d.setDiaChi(rs.getString("dia_chi"));
        return d;
    }

    @Override
    public void add(DayTro t) {
        String sql = """
                INSERT INTO day_tro (id, ma_day, ten_day, dia_chi)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getMaDay());
            ps.setString(3, t.getTenDay());
            if (t.getDiaChi() != null) {
                ps.setString(4, t.getDiaChi());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(DayTro t) {
        String sql = """
                UPDATE day_tro SET ma_day = ?, ten_day = ?, dia_chi = ?
                WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getMaDay());
            ps.setString(2, t.getTenDay());
            if (t.getDiaChi() != null) {
                ps.setString(3, t.getDiaChi());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.setString(4, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Object id) {
        String sql = "DELETE FROM day_tro WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DayTro findById(Object id) {
        String sql = "SELECT id, ma_day, ten_day, dia_chi FROM day_tro WHERE id = ?";
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
    public List<DayTro> getAll() {
        String sql = "SELECT id, ma_day, ten_day, dia_chi FROM day_tro ORDER BY ma_day";
        List<DayTro> list = new ArrayList<>();
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
