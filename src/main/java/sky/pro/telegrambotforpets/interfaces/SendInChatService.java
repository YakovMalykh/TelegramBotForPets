package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.request.Keyboard;
import sky.pro.telegrambotforpets.constants.Buttons;
import sky.pro.telegrambotforpets.constants.Descriptions;
import sky.pro.telegrambotforpets.model.Shelter;

public interface SendInChatService {

    /**
     метод отправляет документ в ответ на запрос
     @param chatId
     @param descriptionFile
     */
    void sendDocumentsForPreparation(Long chatId, Descriptions descriptionFile, Shelter shelter);
    /**
     метод отправляет сообщение в ответ на запрос
     @param chatId
     @param text
     */
    void sendMsg(Long chatId, String text);
    /**
     метод отправляет меню в ответ на запрос
     @param chatId
     @param Menu
     */
    void sendMenu(Long chatId, Keyboard Menu);

    String chouseMenu (Buttons button, Long chatId, Long shelterId);
}