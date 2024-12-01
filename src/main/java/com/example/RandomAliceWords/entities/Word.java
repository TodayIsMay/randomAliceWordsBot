package com.example.RandomAliceWords.entities;

import java.util.List;
import java.util.Objects;

public class Word {
    private Long id;
    private String word;
    private List<String> translations;
    private Long themeId;

    public Word(Long id, String word, List<String> translations, Long themeId) {
        this.id = id;
        this.word = word;
        this.translations = translations;
        this.themeId = themeId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) && Objects.equals(translations, word1.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, translations);
    }

    @Override
    public String toString() {
        return word + " - " + translations + "\n";
    }
}
