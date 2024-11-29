package com.example.RandomAliceWords.app;

import com.example.RandomAliceWords.bot.RandomAliceWordsBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class RandomAliceWordsConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi(RandomAliceWordsBot randomAliceWordsBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(randomAliceWordsBot);
        return api;
    }
}
