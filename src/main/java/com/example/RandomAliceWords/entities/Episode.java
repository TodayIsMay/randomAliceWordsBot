package com.example.RandomAliceWords.entities;

import java.util.Objects;

public class Episode {
    private final Long id;
    private final int number;
    private final Long themeId;

    public Episode(Long id, int number, Long themeId) {
        this.id = id;
        this.number = number;
        this.themeId = themeId;
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public Long getThemeId() {
        return themeId;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", number=" + number +
                ", themeId=" + themeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return number == episode.number && Objects.equals(id, episode.id) && Objects.equals(themeId, episode.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, themeId);
    }
}
