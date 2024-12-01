package com.example.RandomAliceWords.repositories;

import com.example.RandomAliceWords.entities.Word;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WordRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TranslationsRepository translationsRepository;
    private final ThemesRepository themesRepository;

    public WordRepository(JdbcTemplate jdbcTemplate, TranslationsRepository translationsRepository, ThemesRepository themesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.translationsRepository = translationsRepository;
        this.themesRepository = themesRepository;
    }

    public List<Word> getAllWords() {
        String sql = "SELECT * FROM words";
        return jdbcTemplate.query(sql, this::createWord);
    }

    public List<Word> getWordsByThemeId(Long themeId) {
        String sql = "SELECT * FROM themes WHERE theme_id = ?";
        return jdbcTemplate.query(sql, this::createWord);
    }

    private Word createWord(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("word");
        List<String> translations = translationsRepository.getTranslationsForWordByWordId(id);
        Long themeId = rs.getLong("theme_id");
        return new Word(id, name, translations, themeId);
    }
}