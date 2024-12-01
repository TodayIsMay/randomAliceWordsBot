package com.example.RandomAliceWords.enums;

public enum ButtonNames {
    RANDOM_WORD("случайное слово"),

    WORDS_BY_THEMES("слова по темам"),
    ALL_WORDS("все слова");

    private final String buttonName;

    ButtonNames(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}