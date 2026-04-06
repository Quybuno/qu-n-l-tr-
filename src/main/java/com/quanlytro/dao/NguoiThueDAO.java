package com.quanlytro.dao;

import com.quanlytro.model.NguoiThue;
import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NguoiThueDAO implements IGenericDAO<NguoiThue> {

    private static NguoiThue mapRow(ResultSet rs) throws SQLException {
        NguoiThue n = new NguoiThue();
        n.setId(rs.getString("id"));
        n.setHoTen(rs.getString("ho_ten"));
        n.setCmnd(rs.getString("cmnd"));
        n.setSoDienThoai(rs.getString("so_dien_thoai"));
        n.setEmail(rs.getString("email"));
        return n;
    }

    @Override
    public void add(NguoiThue t) {
        String sql = """
                INSERT INTO nguoi_thue (id, ho_ten, cmnd, so_dien_thoai, email)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getHoTen());
            ps.setString(3, t.getCmnd());
            ps.setString(4, t.getSoDienThoai());
            ps.setString(5, t.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NguoiThue t) {
        String sql = """
                UPDATE nguoi_thue SET ho_ten = ?, cmnd = ?, so_dien_thoai = ?, email = ? WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getHoTen());
            ps.setString(2, t.getCmnd());
            ps.setString(3, t.getSoDienThoai());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Object id) {
        String sql = "DELETE FROM nguoi_thue WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NguoiThue findById(Object id) {
        String sql = "SELECT id, ho_ten, cmnd, so_dien_thoai, email FROM nguoi_thue WHERE id = ?";
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
    public List<NguoiThue> getAll() {
        String sql = "SELECT id, ho_ten, cmnd, so_dien_thoai, email FROM nguoi_thue ORDER BY ho_ten";
        List<NguoiThue> list = new ArrayList<>();
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
