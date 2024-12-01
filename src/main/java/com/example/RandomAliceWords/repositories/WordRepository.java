package com.example.RandomAliceWords.repositories;

import com.example.RandomAliceWords.entities.Word;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class WordRepository {
    private JdbcTemplate jdbcTemplate;

    public WordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Word> getAllWords() {
        String sql = "SELECT * FROM words";
        return jdbcTemplate.query(sql, this::createWord);
    }

    private Word createWord(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("word");
        return new Word(name, Set.of());
    }
}