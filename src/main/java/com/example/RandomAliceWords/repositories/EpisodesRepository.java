package com.example.RandomAliceWords.repositories;

import com.example.RandomAliceWords.entities.Episode;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EpisodesRepository {
    private final JdbcTemplate jdbcTemplate;

    public EpisodesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Episode> getEpisodesByThemeId(Long themeId) {
        String sql = "SELECT * FROM episodes WHERE theme_id = ?";
        return jdbcTemplate.query(sql, this::createEpisode, themeId);
    }

    private Episode createEpisode(ResultSet rs, int rowNum) throws SQLException {
        return new Episode(rs.getLong("id"),
                rs.getInt("number"),
                rs.getLong("theme_id"));
    }
}