package com.example.RandomAliceWords.utils;

import com.example.RandomAliceWords.enums.ButtonNames;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

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

    public ReplyKeyboardMarkup getThemesMarkup() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Ben and Holy"));

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(List.of(row1));
        markup.setResizeKeyboard(true);

        return markup;
    }
}
