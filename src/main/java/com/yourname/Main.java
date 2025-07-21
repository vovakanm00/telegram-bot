package com.yourname;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());
            System.out.println("Бот @PractikaSevGU_bot успешно запущен!");
        } catch (Exception e) {
            System.err.println("Ошибка при запуске бота:");
            e.printStackTrace();
        }
    }
}