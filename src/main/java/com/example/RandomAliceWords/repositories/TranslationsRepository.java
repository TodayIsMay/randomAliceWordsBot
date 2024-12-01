package com.example.RandomAliceWords.repositories;

import com.example.RandomAliceWords.entities.Word;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class TranslationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public TranslationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTranslationsForWordByWordId(Long wordId) {
        String sql = "SELECT * FROM translations WHERE word_id =?";
        return jdbcTemplate.query(sql, this::createTranslation, wordId);
    }

    private String createTranslation(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("word_translation");
    }
}