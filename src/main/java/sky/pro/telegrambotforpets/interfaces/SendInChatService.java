package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.request.Keyboard;
import sky.pro.telegrambotforpets.constants.Descriptions;

public interface SendInChatService {

    /**
    метод отправляет документ в ответ на запрос
    @param chatId
    @param descriptionFile
     */
    void sendDoc(Long chatId, Descriptions descriptionFile);
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
}
