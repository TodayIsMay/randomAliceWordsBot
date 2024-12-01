package com.example.RandomAliceWords.enums;

public enum ButtonNames {
    RANDOM_WORD("случайное слово"),

    WORDS_BY_EPISODES("слова по сериям");

    private final String buttonName;

    ButtonNames(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}