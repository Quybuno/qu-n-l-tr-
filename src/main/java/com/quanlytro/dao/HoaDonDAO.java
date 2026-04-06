package com.quanlytro.dao;

import com.quanlytro.model.HoaDon;
import com.quanlytro.model.enums.TrangThaiHoaDon;
import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO implements IGenericDAO<HoaDon> {

    private final HopDongDAO hopDongDAO;

    public HoaDonDAO(HopDongDAO hopDongDAO) {
        this.hopDongDAO = hopDongDAO;
    }

    private static HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon h = new HoaDon();
        h.setId(rs.getString("id"));
        h.setMaHoaDon(rs.getString("ma_hoa_don"));
        h.setHopDongId(rs.getString("hop_dong_id"));
        h.setNam(rs.getInt("nam"));
        h.setThang(rs.getInt("thang"));
        h.setTienPhong(rs.getBigDecimal("tien_phong"));
        h.setTienDien(rs.getBigDecimal("tien_dien"));
        h.setTienNuoc(rs.getBigDecimal("tien_nuoc"));
        h.setTongTien(rs.getBigDecimal("tong_tien"));
        h.setTrangThai(TrangThaiHoaDon.valueOf(rs.getString("trang_thai")));
        return h;
    }

    private HoaDon hydrate(HoaDon h) {
        if (h == null) {
            return null;
        }
        if (h.getHopDongId() != null) {
            h.setHopDong(hopDongDAO.findById(h.getHopDongId()));
        }
        return h;
    }

    @Override
    public void add(HoaDon t) {
        String sql = """
                INSERT INTO hoa_don (id, ma_hoa_don, hop_dong_id, nam, thang, tien_phong, tien_dien, tien_nuoc, tong_tien, trang_thai)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getMaHoaDon());
            ps.setString(3, t.getHopDongId());
            ps.setInt(4, t.getNam());
            ps.setInt(5, t.getThang());
            ps.setBigDecimal(6, t.getTienPhong());
            ps.setBigDecimal(7, t.getTienDien());
            ps.setBigDecimal(8, t.getTienNuoc());
            ps.setBigDecimal(9, t.getTongTien());
            ps.setString(10, t.getTrangThai().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(HoaDon t) {
        String sql = """
                UPDATE hoa_don SET ma_hoa_don = ?, hop_dong_id = ?, nam = ?, thang = ?, tien_phong = ?, tien_dien = ?, tien_nuoc = ?, tong_tien = ?, trang_thai = ?
                WHERE id = ?
                """;
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getMaHoaDon());
            ps.setString(2, t.getHopDongId());
            ps.setInt(3, t.getNam());
            ps.setInt(4, t.getThang());
            ps.setBigDecimal(5, t.getTienPhong());
            ps.setBigDecimal(6, t.getTienDien());
            ps.setBigDecimal(7, t.getTienNuoc());
            ps.setBigDecimal(8, t.getTongTien());
            ps.setString(9, t.getTrangThai().name());
            ps.setString(10, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Object id) {
        String sql = "DELETE FROM hoa_don WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HoaDon findById(Object id) {
        String sql = """
                SELECT id, ma_hoa_don, hop_dong_id, nam, thang, tien_phong, tien_dien, tien_nuoc, tong_tien, trang_thai
                FROM hoa_don WHERE id = ?
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
    public List<HoaDon> getAll() {
        String sql = """
                SELECT id, ma_hoa_don, hop_dong_id, nam, thang, tien_phong, tien_dien, tien_nuoc, tong_tien, trang_thai
                FROM hoa_don ORDER BY nam DESC, thang DESC
                """;
        List<HoaDon> list = new ArrayList<>();
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
}
