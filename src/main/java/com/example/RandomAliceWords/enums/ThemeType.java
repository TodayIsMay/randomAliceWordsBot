package com.example.RandomAliceWords.enums;

public enum ThemeType {
    SERIES("Сериал"),
    FILM("Фильм");

    private String name;

    ThemeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
