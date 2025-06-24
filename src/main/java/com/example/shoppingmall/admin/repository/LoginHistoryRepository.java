package com.example.shoppingmall.admin.repository;

import com.example.shoppingmall.admin.entity.LoginHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LoginHistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public LoginHistoryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<LoginHistory> loginHistoryRowMapper = (rs, rowNum) -> {
        LoginHistory history = new LoginHistory();
        history.setId(rs.getLong("id"));
        history.setRole(rs.getString("role"));
        history.setUserId(rs.getString("user_id"));
        history.setName(rs.getString("name"));
        history.setLoginDateTime(rs.getTimestamp("login_date_time").toLocalDateTime());
        history.setUserRole(rs.getString("user_role"));
        history.setStatus(rs.getString("status"));
        return history;
    };

    public List<LoginHistory> findBySearchCriteria(
            String startDate, String endDate, String role,
            String name, String status, int offset, int limit) {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM login_history WHERE 1=1");

        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND login_date_time >= ?");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND login_date_time <= ?");
        }
        if (role != null && !role.isEmpty()) {
            sql.append(" AND role = ?");
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        sql.append(" ORDER BY login_date_time DESC LIMIT ? OFFSET ?");

        return jdbcTemplate.query(sql.toString(),
                ps -> {
                    int index = 1;
                    if (startDate != null && !startDate.isEmpty()) {
                        ps.setString(index++, startDate + " 00:00:00");
                    }
                    if (endDate != null && !endDate.isEmpty()) {
                        ps.setString(index++, endDate + " 23:59:59");
                    }
                    if (role != null && !role.isEmpty()) {
                        ps.setString(index++, role);
                    }
                    if (name != null && !name.isEmpty()) {
                        ps.setString(index++, "%" + name + "%");
                    }
                    if (status != null && !status.isEmpty()) {
                        ps.setString(index++, status);
                    }
                    ps.setInt(index++, limit);
                    ps.setInt(index, offset);
                },
                loginHistoryRowMapper
        );
    }

    public int countBySearchCriteria(
            String startDate, String endDate, String role,
            String name, String status) {

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM login_history WHERE 1=1");

        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND login_date_time >= ?");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND login_date_time <= ?");
        }
        if (role != null && !role.isEmpty()) {
            sql.append(" AND role = ?");
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }

        return jdbcTemplate.queryForObject(sql.toString(),
                (rs, rowNum) -> rs.getInt(1),
                startDate != null && !startDate.isEmpty() ? startDate + " 00:00:00" : null,
                endDate != null && !endDate.isEmpty() ? endDate + " 23:59:59" : null,
                role,
                name != null ? "%" + name + "%" : null,
                status
        );
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM login_history WHERE status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, status);
    }
}