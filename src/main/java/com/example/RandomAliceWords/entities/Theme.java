package com.example.RandomAliceWords.entities;

import com.example.RandomAliceWords.enums.ThemeType;

public class Theme {
    private Long id;
    private String name;
    private ThemeType themeType;
    private String prefix;

    public Theme(Long id, String name, ThemeType themeType, String prefix) {
        this.id = id;
        this.name = name;
        this.themeType = themeType;
        this.prefix = prefix;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThemeType getThemeType() {
        return themeType;
    }

    public void setThemeType(ThemeType themeType) {
        this.themeType = themeType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
