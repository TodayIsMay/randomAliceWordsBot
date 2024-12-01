package com.example.RandomAliceWords.repositories;

import com.example.RandomAliceWords.entities.Word;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WordRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TranslationsRepository translationsRepository;

    public WordRepository(JdbcTemplate jdbcTemplate, TranslationsRepository translationsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.translationsRepository = translationsRepository;
    }

    public List<Word> getAllWords() {
        String sql = "SELECT * FROM words";
        return jdbcTemplate.query(sql, this::createWord);
    }

    private Word createWord(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("word");
        List<String> translations = translationsRepository.getTranslationsForWordByWordId(id);
        return new Word(id, name, translations);
    }
}