package com.quanlytro.db;

import com.quanlytro.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Cap nhat schema khi DB da tao tu phien ban cu (chua co cot don gia / bang chi so).
 */
public final class SchemaMigrator {

    /** MySQL / MariaDB: ER_DUP_FIELDNAME */
    private static final int DUPLICATE_COLUMN = 1060;

    public static void ensureLatest() {
        try (Connection c = DatabaseUtil.getConnection()) {
            addColumnIgnoreDuplicate(c, """
                    ALTER TABLE hop_dong ADD COLUMN gia_dien_moi_so DECIMAL(15, 4) NOT NULL DEFAULT 3500
                    """);
            addColumnIgnoreDuplicate(c, """
                    ALTER TABLE hop_dong ADD COLUMN gia_nuoc_moi_khoi DECIMAL(15, 4) NOT NULL DEFAULT 18000
                    """);
            createChiSoTableIfNotExists(c);
        } catch (SQLException e) {
            throw new RuntimeException("Khong the cap nhat schema CSDL. Kiem tra ket noi va quyen ALTER.", e);
        }
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
