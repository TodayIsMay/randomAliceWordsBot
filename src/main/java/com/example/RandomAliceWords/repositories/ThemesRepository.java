package com.example.RandomAliceWords.repositories;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ThemesRepository {
    private final JdbcTemplate jdbcTemplate;

    public ThemesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getThemeById(Long themeId) {
        String sql = "SELECT * FROM themes WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::createTheme, themeId);
    }

    private String createTheme(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("word_translation");
    }
}