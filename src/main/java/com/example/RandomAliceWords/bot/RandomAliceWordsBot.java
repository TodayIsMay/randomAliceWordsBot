package com.example.RandomAliceWords.bot;

import com.example.RandomAliceWords.entities.Episode;
import com.example.RandomAliceWords.entities.Theme;
import com.example.RandomAliceWords.entities.Word;
import com.example.RandomAliceWords.enums.ButtonNames;
import com.example.RandomAliceWords.repositories.EpisodesRepository;
import com.example.RandomAliceWords.repositories.ThemesRepository;
import com.example.RandomAliceWords.repositories.TranslationsRepository;
import com.example.RandomAliceWords.repositories.WordRepository;
import com.example.RandomAliceWords.utils.ReplyKeyboardMaker;
import org.apache.logging.log4j.util.Strings;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final WordRepository wordRepository;
    private final TranslationsRepository translationsRepository;
    private final ThemesRepository themesRepository;
    private final EpisodesRepository episodesRepository;
    private ReplyKeyboardMarkup themesChoiceMarkup;

    public RandomAliceWordsBot(@Value("${bot.token}") String botToken) {
        super(botToken);
        String url = "jdbc:postgresql://db:5432/db?user=aliceBot&password=fx9@CyVXH1";
        var dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        translationsRepository = new TranslationsRepository(jdbcTemplate);
        themesRepository = new ThemesRepository(jdbcTemplate);
        wordRepository = new WordRepository(jdbcTemplate, translationsRepository, themesRepository);
        episodesRepository = new EpisodesRepository(jdbcTemplate);
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
            sendMessageWithKeyboard(chatId, "Yes!", mainMenuMarkup);
            vocabulary.remove(word);
            return;
        } else if (message.equals(task.get(word))) {
            sendMessageWithKeyboard(chatId, "Yes!", mainMenuMarkup);
            task.remove(word);
            return;
        }
        if (message.equals(ButtonNames.RANDOM_WORD.getButtonName())) {
            nextWord(chatId);
        } else if (message.equals(ButtonNames.WORDS_BY_THEMES.getButtonName())) {
            wordsByThemes(chatId);
        } else if (message.equals(START)) {
            String userName = update.getMessage().getChat().getUserName();
            startCommand(chatId, userName);
        } else if (message.equals(ButtonNames.ALL_WORDS.getButtonName())) {
            getAllWords(chatId);
        } else if (message.equals("Ben and Holy".toLowerCase(Locale.ROOT))) {
            showEpisodeChoosingMessage(chatId, "Ben and Holy");
        } else if (message.contains("ep")) {
            getWordsByEpisodes(chatId, message);
        } else {
            unknownCommand(chatId);
        }
    }

    private void getWordsByEpisodes(Long chatId, String message) {
        String[] split = message.split("_");
        String themePrefix = new StringBuilder(split[0]).substring(1);
        Theme theme = themesRepository.getAllThemes()
                .stream()
                .filter(th -> th.getPrefix().equals(themePrefix))
                .findFirst()
                .orElseThrow();
        Integer episodeNumber = Integer.valueOf(split[2]);
        List<Word> words = wordRepository.getWordsByThemeId(theme.getId())
                .stream()
                .filter(w -> w.getEpisodeNumber().equals(episodeNumber))
                .collect(Collectors.toList());
        sendMessageWithKeyboard(chatId, words.toString(), mainMenuMarkup);
    }

    private void getAllWords(Long chatId) {
        List<Word> words = wordRepository.getAllWords();
        sendMessageWithKeyboard(chatId, words.toString(), mainMenuMarkup);
    }

    private void nextWord(Long chatId) {
        log.info("Words in the map: {}", vocabulary.size());
        if (vocabulary.size() == 0) {
            sendMessageWithKeyboard(chatId, "Всё, слова кончились. Можно нажать /start и попробовать начать сначала.", mainMenuMarkup);
            return;
        }
        Random random = new Random();
        int index = random.nextInt(vocabulary.size());
        word = vocabulary.keySet().stream().toList().get(index);
        log.info("Word is: {}", word);
        sendMessageWithKeyboard(chatId, word, mainMenuMarkup);
    }

    private void wordsByThemes(Long chatId) {
        themesChoiceMarkup = keyboardMaker.getThemesMarkup(themesRepository);
        sendMessageWithKeyboard(chatId, "Выбери тему:", themesChoiceMarkup);
    }

    private void getWordsByChosenTheme(Long chatId, Long themeId) {
        sendMessageWithKeyboard(chatId, wordRepository.getWordsByThemeId(themeId).toString(), mainMenuMarkup);
    }

    private void showEpisodeChoosingMessage(Long chatId, String themeName) {
        Theme theme = themesRepository.getThemeByName(themeName);
        List<Episode> episodes = episodesRepository.getEpisodesByThemeId(theme.getId());
        StringBuilder messageBuilder = new StringBuilder("Выбери эпизод: \n");
        for (int i = 1; i <= episodes.size(); i++) {
            messageBuilder//TODO: keep it somewhere - command for choosing episode should be the following:
                    // / + themePrefix + '_ep_' + i(number of the episode)
                    .append("Эпизод ")
                    .append(i)
                    .append(" /")
                    .append(theme.getPrefix())
                    .append("_ep_")
                    .append(i)
                    .append("\n");
        }
        sendMessage(chatId, messageBuilder.toString());
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
        sendMessageWithKeyboard(chatId, text, mainMenuMarkup);
    }

    private void showAllWords(Long chatId) {
        var text = vocabulary.entrySet().toString();
        sendMessageWithKeyboard(chatId, text, mainMenuMarkup);
    }

    private void nextTask(Long chatId) {
        log.info("Tasks in the map: {}", task.size());
        if (task.size() == 0) {
            sendMessageWithKeyboard(chatId, "Всё, задания кончились. Можно нажать /start и попробовать начать сначала.", mainMenuMarkup);
            return;
        }
        Random random = new Random();
        int index = random.nextInt(task.size());
        word = new ArrayList<>(task.keySet()).get(index);
        log.info("Word is: {}", word);
        sendMessageWithKeyboard(chatId, word, mainMenuMarkup);
    }

    private void unknownCommand(Long chatId) {
        var text = "Ну это какая-то неправильная команда.";
        sendMessageWithKeyboard(chatId, text, mainMenuMarkup);
    }

    private void sendMessageWithKeyboard(Long chatId, String text, ReplyKeyboardMarkup replyKeyboard) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        sendMessage.setReplyMarkup(replyKeyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Sending message error");
        }
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
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