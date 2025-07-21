package com.yourname;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {
    private List<Equipment> equipmentList = new ArrayList<>();
    private String lastCommand = "";

    @Override
    public String getBotUsername() {
        return "PractikaSevGU_bot";
    }

    @Override
    public String getBotToken() {
        return "7736079053:AAEUEznZm-RXvAJ180XPhnsPdDIKeNsQyf8";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    sendMainMenu(chatId);
                    lastCommand = "";
                    break;
                case "📋 Добавить оборудование":
                    sendMessage(chatId, "Введите данные в формате:\nМодель СерийныйНомер\n\nПример: Холодильник X-200 ABC123");
                    lastCommand = "add";
                    break;
                case "📂 Список оборудования":
                    showEquipmentList(chatId);
                    lastCommand = "";
                    break;
                case "🔍 Найти оборудование":
                    sendMessage(chatId, "Введите серийный номер для поиска:");
                    lastCommand = "search";
                    break;
                case "❌ Удалить оборудование":
                    sendMessage(chatId, "Введите серийный номер для удаления:");
                    lastCommand = "delete";
                    break;
                default:
                    processUserInput(chatId, messageText);
            }
        }
    }

    private void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("🏭 Бот для учета холодильного оборудования\nВыберите действие:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📋 Добавить оборудование");
        row1.add("📂 Список оборудования");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🔍 Найти оборудование");
        row2.add("❌ Удалить оборудование");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void processUserInput(long chatId, String text) {
        if (lastCommand.equals("delete")) {
            boolean removed = equipmentList.removeIf(e -> e.getSerialNumber().equalsIgnoreCase(text));
            sendMessage(chatId, removed ?
                    "✅ Оборудование с серийным номером " + text + " удалено!" :
                    "⚠️ Оборудование с таким серийным номером не найдено!");
            sendMainMenu(chatId);
            lastCommand = "";
        }
        else if (lastCommand.equals("search")) {
            equipmentList.stream()
                    .filter(e -> e.getSerialNumber().equalsIgnoreCase(text))
                    .findFirst()
                    .ifPresentOrElse(
                            e -> sendMessage(chatId, "🔍 Найдено оборудование:\n" + e),
                            () -> sendMessage(chatId, "⚠️ Оборудование не найдено!")
                    );
            sendMainMenu(chatId);
            lastCommand = "";
        }
        else if (text.matches(".+\\s.+")) {
            String[] parts = text.split(" ", 2);
            equipmentList.add(new Equipment(parts[0], parts[1]));
            sendMessage(chatId, "✅ Оборудование добавлено!\nМодель: " + parts[0] + "\nСерийный номер: " + parts[1]);
            sendMainMenu(chatId);
        }
        else {
            sendMessage(chatId, "Неизвестная команда. Используйте меню.");
            sendMainMenu(chatId);
        }
    }

    private void showEquipmentList(long chatId) {
        if (equipmentList.isEmpty()) {
            sendMessage(chatId, "📭 Список оборудования пуст");
        } else {
            StringBuilder sb = new StringBuilder("📋 Список оборудования:\n\n");
            equipmentList.forEach(e -> sb.append(e).append("\n\n"));
            sendMessage(chatId, sb.toString());
        }
        sendMainMenu(chatId);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}