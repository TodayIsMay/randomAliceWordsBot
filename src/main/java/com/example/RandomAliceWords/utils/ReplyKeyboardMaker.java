package com.example.RandomAliceWords.utils;

import com.example.RandomAliceWords.enums.ButtonNames;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        SendMessage message = new SendMessage();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Случайное слово"));
        row1.add(new KeyboardButton("Слова по сериям"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNames.ALL_WORDS.getButtonName()));

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(List.of(row1, row2));
        markup.setResizeKeyboard(true);
        message.setReplyMarkup(markup);

        return markup;
    }
}
