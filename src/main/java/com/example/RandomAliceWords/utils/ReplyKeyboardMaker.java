package com.example.RandomAliceWords.utils;

import com.example.RandomAliceWords.entities.Theme;
import com.example.RandomAliceWords.repositories.ThemesRepository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Случайное слово"));
        row1.add(new KeyboardButton("Слова по темам"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Все слова"));

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(List.of(row1, row2));
        markup.setResizeKeyboard(true);

        return markup;
    }

    public ReplyKeyboardMarkup getThemesMarkup(ThemesRepository themesRepository) {
        List<Theme> themes = themesRepository.getAllThemes();
        List<KeyboardRow> rows = new ArrayList<>();

        for (Theme theme : themes) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(theme.getName()));
            rows.add(row);
        }

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    /**
     * Returns the list of buttons with episodes' numbers
     * @param amount from 1 till IntMax
     * @return {@link ReplyKeyboardMarkup}
     */
    public ReplyKeyboardMarkup getEpisodesMarkup(int amount) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(valueOf(i)));
            rows.add(row);
        }

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        markup.setResizeKeyboard(true);

        return markup;
    }
}