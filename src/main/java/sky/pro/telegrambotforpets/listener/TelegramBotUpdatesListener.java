package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.services.GuestServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

import static sky.pro.telegrambotforpets.constants.Constants.*;

/**
 * отвечает за работу телеграм бота
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {


    @Autowired
    private TelegramBot telegramBot;

    private final GuestService guestService;

    public TelegramBotUpdatesListener(GuestServiceImpl guestService) {
        this.guestService = guestService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

            if (!guestService.doesGuestAlreadyExistsInDB(update)) {
                guestService.saveGuestToDB(update);
            }
            switch (update.message().text()) {
                case MENU_1_BUTTON_1:
                    //Здесь будет ответ на запрос О приюте
                    sendMsg(update, "Информация о приюте");
                    break;
                case MENU_1_BUTTON_2:
                    //Здесь будет ответ на запрос Как взять питомца
                    sendMsg(update, "Как взять питомца из приюта");
                    break;
                case MENU_1_BUTTON_3:
                    //Здесь будет Отправка отчета
                    sendMsg(update, "Отправить отчет о питомце");
                    break;
                case MENU_1_BUTTON_4:
                    //Здесь будем звать волонтера
                    sendMsg(update, "Позвать волонтера");
                    break;
                default:
                    //В ответ на неопознанную команду выдает меню
                    sendMenu(update.message().chat().id());
                    break;
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendMsg(Update update, String text) {
        Long chatId = update.message().chat().id();
        SendMessage message = new SendMessage(chatId, text);
        SendResponse responce = telegramBot.execute(message);
        System.out.println(responce.isOk());
        System.out.println(responce.errorCode());

    }

    private void sendMenu(long chatId) {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{MENU_1_BUTTON_1, MENU_1_BUTTON_2},
                new String[]{MENU_1_BUTTON_3, MENU_1_BUTTON_4})
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);

        SendMessage request = new SendMessage(chatId, "Выберите пункт меню")
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);

        SendResponse sendResponse = telegramBot.execute(request);
        if (!sendResponse.isOk()) {
            int codeError = sendResponse.errorCode();
            String description = sendResponse.description();
        }
    }
}