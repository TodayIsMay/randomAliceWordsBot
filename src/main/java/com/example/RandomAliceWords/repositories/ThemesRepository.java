package com.example.RandomAliceWords.repositories;

import com.example.RandomAliceWords.entities.Theme;
import com.example.RandomAliceWords.enums.ThemeType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ThemesRepository {
    private final JdbcTemplate jdbcTemplate;

    public ThemesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme getThemeById(Long themeId) {
        String sql = "SELECT * FROM themes WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::createTheme, themeId);
    }

    public List<Theme> getAllThemes() {
        String sql = "SELECT * FROM themes";
        return jdbcTemplate.query(sql, this::createTheme);
    }

    private Theme createTheme(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(rs.getLong("id"),
                rs.getString("name"),
                ThemeType.valueOf(rs.getString("type")),
                rs.getString("prefix"));
    }
}