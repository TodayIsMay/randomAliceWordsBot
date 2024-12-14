package com.example.RandomAliceWords.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMaker {
    public static InlineKeyboardMarkup getLearnReadInlineMarkup(String episodeIdentifier) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        {
            InlineKeyboardButton inlineButtonRead = new InlineKeyboardButton("Посмотреть");
            inlineButtonRead.setCallbackData(episodeIdentifier + ", LOOK");
            buttons.add(List.of(inlineButtonRead));
        }

        {
            InlineKeyboardButton inlineButtonLearn = new InlineKeyboardButton("Учить");
            inlineButtonLearn.setCallbackData(episodeIdentifier + ", LEARN");
            buttons.add(List.of(inlineButtonLearn));
        }

        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
