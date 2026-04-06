package com.quanlytro.db;

import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Cap nhat schema khi DB da tao tu phien ban cu (chua co cot don gia / bang chi so / day tro).
 */
public final class SchemaMigrator {

    /** MySQL / MariaDB: ER_DUP_FIELDNAME */
    private static final int DUPLICATE_COLUMN = 1060;

    /** Mac dinh cho DB cu chua co bang day_tro */
    public static final String DEFAULT_DAY_TRO_ID = "11111111-1111-1111-1111-111111111111";

    public static void ensureLatest() {
        try (Connection c = DatabaseUtil.getConnection()) {
            addColumnIgnoreDuplicate(c, """
                    ALTER TABLE hop_dong ADD COLUMN gia_dien_moi_so DECIMAL(15, 4) NOT NULL DEFAULT 3500
                    """);
            addColumnIgnoreDuplicate(c, """
                    ALTER TABLE hop_dong ADD COLUMN gia_nuoc_moi_khoi DECIMAL(15, 4) NOT NULL DEFAULT 18000
                    """);
            createChiSoTableIfNotExists(c);
            ensureDayTroAndPhong(c);
        } catch (SQLException e) {
            throw new RuntimeException("Khong the cap nhat schema CSDL. Kiem tra ket noi va quyen ALTER.", e);
        }
    }

    private static void ensureDayTroAndPhong(Connection c) throws SQLException {
        createDayTroTableIfNotExists(c);
        if (countRows(c, "day_tro") == 0) {
            insertDefaultDayTro(c);
        }
        addPhongDayTroColumnIfNeeded(c);
        backfillPhongDayTro(c);
        setPhongDayTroNotNull(c);
        dropOldMaPhongUniqueIfExists(c);
        addCompositeUniqueIfNeeded(c);
        addPhongDayForeignKeyIfNeeded(c);
    }

    private static void createDayTroTableIfNotExists(Connection c) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS day_tro (
                    id VARCHAR(36) PRIMARY KEY,
                    ma_day VARCHAR(32) NOT NULL,
                    ten_day VARCHAR(128) NOT NULL,
                    dia_chi VARCHAR(512),
                    UNIQUE KEY uk_ma_day (ma_day)
                )
                """;
        try (Statement st = c.createStatement()) {
            st.execute(sql);
        }
    }

    private static int countRows(Connection c, String table) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private static void insertDefaultDayTro(Connection c) throws SQLException {
        String sql = "INSERT INTO day_tro (id, ma_day, ten_day) VALUES (?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, DEFAULT_DAY_TRO_ID);
            ps.setString(2, "DAY01");
            ps.setString(3, "Day tro mac dinh");
            ps.executeUpdate();
        }
    }

    private static void addPhongDayTroColumnIfNeeded(Connection c) throws SQLException {
        addColumnIgnoreDuplicate(c, """
                ALTER TABLE phong_tro ADD COLUMN day_tro_id VARCHAR(36) NULL
                """);
    }

    private static void backfillPhongDayTro(Connection c) throws SQLException {
        String defaultId;
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(
                "SELECT id FROM day_tro ORDER BY ma_day LIMIT 1")) {
            if (!rs.next()) {
                return;
            }
            defaultId = rs.getString(1);
        }
        String upd = "UPDATE phong_tro SET day_tro_id = ? WHERE day_tro_id IS NULL";
        try (PreparedStatement ps = c.prepareStatement(upd)) {
            ps.setString(1, defaultId);
            ps.executeUpdate();
        }
    }

    private static void setPhongDayTroNotNull(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("ALTER TABLE phong_tro MODIFY COLUMN day_tro_id VARCHAR(36) NOT NULL");
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Invalid use of NULL value") || msg.contains("cannot be null")) {
                throw new SQLException("Khong the dat day_tro_id NOT NULL — con phong chua gan day.", e);
            }
        }
    }

    private static void dropOldMaPhongUniqueIfExists(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("ALTER TABLE phong_tro DROP INDEX uk_ma_phong");
        } catch (SQLException e) {
            ignoreIfCantDropIndex(e);
        }
    }

    private static void addCompositeUniqueIfNeeded(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("""
                    ALTER TABLE phong_tro ADD UNIQUE KEY uk_day_ma_phong (day_tro_id, ma_phong)
                    """);
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (e.getErrorCode() == 1061 || msg.contains("Duplicate key name") || msg.contains("already exists")) {
                return;
            }
            throw e;
        }
    }

    private static void addPhongDayForeignKeyIfNeeded(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("""
                    ALTER TABLE phong_tro
                    ADD CONSTRAINT fk_pt_day FOREIGN KEY (day_tro_id) REFERENCES day_tro (id)
                    """);
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (msg.contains("Duplicate foreign key") || msg.contains("already exists")
                    || e.getErrorCode() == 1826 || msg.contains("fk_pt_day")) {
                return;
            }
            throw e;
        }
    }

    private static void ignoreIfCantDropIndex(SQLException e) throws SQLException {
        String msg = e.getMessage() != null ? e.getMessage() : "";
        if (msg.contains("check that column/key exists") || msg.contains("Can't DROP")
                || e.getErrorCode() == 1091) {
            return;
        }
        throw e;
    }

    private static void addColumnIgnoreDuplicate(Connection c, String sql) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (e.getErrorCode() != DUPLICATE_COLUMN && !msg.contains("Duplicate column")) {
                throw e;
            }
        }
    }

    private static void createChiSoTableIfNotExists(Connection c) throws SQLException {
        String sql = """
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
                )
                """;
        try (Statement st = c.createStatement()) {
            st.execute(sql);
        }
    }

    private SchemaMigrator() {
    }
}
