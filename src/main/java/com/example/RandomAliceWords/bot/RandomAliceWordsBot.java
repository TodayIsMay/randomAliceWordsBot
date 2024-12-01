package com.example.RandomAliceWords.bot;

import com.example.RandomAliceWords.enums.ButtonNames;
import com.example.RandomAliceWords.utils.ReplyKeyboardMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RandomAliceWordsBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(RandomAliceWordsBot.class);
    private static final String START = "/start";
    private static final String NEXT_WORD = "/next_word";
    private static final String ALL_WORDS = "/all_words";
    private static final String TASK = "/task";
    private final Map<String, String> vocabulary = new HashMap<>();//слово англ - перевод
    private final Map<String, String> task = new HashMap<>();
    private String word = "";
    private final ReplyKeyboardMaker keyboardMaker = new ReplyKeyboardMaker();
    private final ReplyKeyboardMarkup mainMenuMarkup = keyboardMaker.getMainMenuKeyboard();

    public RandomAliceWordsBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText().trim().toLowerCase(Locale.ROOT);
        log.info("Message was: {}", message);
        Long chatId = update.getMessage().getChatId();
        if (message.equals(vocabulary.get(word))) {
            sendMessage(chatId, "Yes!", mainMenuMarkup);
            vocabulary.remove(word);
            return;
        } else if (message.equals(task.get(word))) {
            sendMessage(chatId, "Yes!", mainMenuMarkup);
            task.remove(word);
            return;
        }

        if (message.equals(ButtonNames.RANDOM_WORD.getButtonName())) {
            nextWord(chatId);
        } else if (message.equals(ButtonNames.WORDS_BY_EPISODES.getButtonName())) {
            wordsByEpisodes(chatId);
        } else if (message.equals(START)) {
            String userName = update.getMessage().getChat().getUserName();
            startCommand(chatId, userName);
        } else {
            unknownCommand(chatId);
        }
    }

    private void nextWord(Long chatId) {
        log.info("Words in the map: {}", vocabulary.size());
        if (vocabulary.size() == 0) {
            sendMessage(chatId, "Всё, слова кончились. Можно нажать /start и попробовать начать сначала.", mainMenuMarkup);
            return;
        }
        Random random = new Random();
        int index = random.nextInt(vocabulary.size());
        word = vocabulary.keySet().stream().toList().get(index);
        log.info("Word is: {}", word);
        sendMessage(chatId, word, mainMenuMarkup);
    }

    private void wordsByEpisodes(Long chatId) {
        sendMessage(chatId, "Пока не готово", mainMenuMarkup);
    }

    private void startCommand(Long chatId, String userName) {
//        vocabulary.put("try", "пытаться");
//        vocabulary.put("enjoy", "наслаждаться");
//        vocabulary.put("suffer", "страдать");
//        vocabulary.put("again", "опять");
//        vocabulary.put("expensive", "дорогой");
//        vocabulary.put("egg", "яйцо");
//        vocabulary.put("later", "позже");
//        vocabulary.put("tree", "дерево");
//        vocabulary.put("several", "несколько");
//        vocabulary.put("help", "помогать");
//        vocabulary.put("lesson", "урок");
//        vocabulary.put("hope", "надеяться");
//        vocabulary.put("new", "новый");
//        vocabulary.put("tell", "говорить");
//        vocabulary.put("sound", "звук");
//        vocabulary.put("by the way", "кстати");
//        vocabulary.put("kind", "вид");
//        vocabulary.put("come up", "приближаться");
//        vocabulary.put("So long!", "прощай");
//        vocabulary.put("Good luck!", "удачи");
//        vocabulary.put("feed", "кормить");
//        vocabulary.put("too", "слишком");
//        vocabulary.put("familiar", "знакомый");
//        vocabulary.put("wait", "ждать");
//        vocabulary.put("float", "плавать");
//        vocabulary.put("find out", "выяснять");
//        vocabulary.put("yell", "кричать");
//        vocabulary.put("excited", "взволнован");
//        vocabulary.put("attention", "внимание");
//        vocabulary.put("neighbourhood", "окрестности");
        vocabulary.put("adventure", "приключение");
        vocabulary.put("royal", "королевский");
        vocabulary.put("thistle", "чертополох");
        vocabulary.put("fairy", "фея");
        vocabulary.put("impressive", "впечатляющий");
        vocabulary.put("rotten", "гнилой");
        vocabulary.put("give it a miss", "пропустить");
        vocabulary.put("plenty of something", "очень много");
        vocabulary.put("feel like something", "хотеть");
        vocabulary.put("dollop", "порция");

        {
            task.put("Today's _________ starts at the little castle", "adventure");
            task.put("Maybe we should ____ the magic jelly _ ____ this year", "give a miss");
            task.put("You need ______ __ food so you have enough to share", "plenty of");
            task.put("I ____ ____ some sparkling fairy juice", "feel like");
            task.put("Lemonade with a ______ of ice-cream", "dollop");
        }
        var text = "Привет! Здесь можно повторять слова перед занятиями. :)\n Добавлять новые пока нельзя, но мы работаем над этим. \n Просто пиши ответ боту и он скажет, правильно или нет.";
        sendMessage(chatId, text, mainMenuMarkup);
    }

    private void showAllWords(Long chatId) {
        var text = vocabulary.entrySet().toString();
        sendMessage(chatId, text, mainMenuMarkup);
    }

    private void nextTask(Long chatId) {
        log.info("Tasks in the map: {}", task.size());
        if (task.size() == 0) {
            sendMessage(chatId, "Всё, задания кончились. Можно нажать /start и попробовать начать сначала.", mainMenuMarkup);
            return;
        }
        Random random = new Random();
        int index = random.nextInt(task.size());
        word = new ArrayList<>(task.keySet()).get(index);
        log.info("Word is: {}", word);
        sendMessage(chatId, word, mainMenuMarkup);
    }

    private void unknownCommand(Long chatId) {
        var text = "Ну это какая-то неправильная команда.";
        sendMessage(chatId, text, mainMenuMarkup);
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup replyKeyboard) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        sendMessage.setReplyMarkup(replyKeyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Sending message error");
        }
    }

    @Override
    public String getBotUsername() {
        return "random_alice_words_bot";
    }
}