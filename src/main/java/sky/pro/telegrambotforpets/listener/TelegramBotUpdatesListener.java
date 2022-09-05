package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Descriptions;
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
                    sendMsg(update, "Информация о приюте");
                    sendMenu(Menu0_1(), update.message().chat().id());
                    break;
                case MENU_1_1_BUTTON_1:
                    sendMsg(update, "Здесь будем выдавать описание приюта");
                    break;
                case MENU_1_1_BUTTON_2:
                    sendMsg(update, "Расписание и адрес приюта");
                    break;
                case MENU_1_1_BUTTON_3:
                    sendMsg(update, "Техника безопасности на территории приюта");
                    break;
                case MENU_1_1_BUTTON_4:
                    sendMsg(update, "Оставить контакт");
                    break;
                case MENU_1_BUTTON_2:
                    //Здесь будет ответ на запрос Как взять питомца
                    sendMsg(update, "Как взять питомца из приюта");
                    sendMenu(Menu0_2(), update.message().chat().id());
                    break;
                case MENU_1_2_BUTTON_1:
                    sendMsg(update, "Правила знакомства с питомцем");
                    break;
                case MENU_1_2_BUTTON_2:
                    sendMsg(update, "Документы необходимые, чтобы взять питомца");
                    break;
                case MENU_1_2_BUTTON_3:
                    sendMsg(update, "Рекомендации по транспортировке питомца");
                    break;
                case MENU_1_2_BUTTON_4:
                    sendMsg(update, "Рекомендации по обустройству дома для щенка");
                    break;
                case MENU_1_2_BUTTON_5:
                    sendMsg(update, "Рекомендации по обустройству дома для взрослой собаки");
                    break;
                case MENU_1_2_BUTTON_6:
                    sendMsg(update, "Рекомендации по обустройству дома для собаки с ограниченными возможностями");
                    break;
                case MENU_1_2_BUTTON_7:
                    sendMsg(update, "Советы кинолога по первичному общению с питомцем");
                    break;
                case MENU_1_2_BUTTON_8:
                    sendMsg(update, "Список проверенных кинологов");
                    break;
                case MENU_1_2_BUTTON_9:
                    sendMsg(update, "Причины отказа в заборе питомца");
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
                    sendMenu(Menu0(), update.message().chat().id());
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

    private void sendMenu(Keyboard Menu, long chatId) {
        Keyboard replyKeyboardMarkup = Menu;
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

    private Keyboard Menu0() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{MENU_1_BUTTON_1, MENU_1_BUTTON_2},
                new String[]{MENU_1_BUTTON_3, MENU_1_BUTTON_4})
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }

    private Keyboard Menu0_1() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{MENU_1_1_BUTTON_1, MENU_1_1_BUTTON_2},
                new String[]{MENU_1_1_BUTTON_3},
                new String[]{MENU_1_1_BUTTON_4},
                new String[]{MENU_1_BUTTON_3, MENU_1_BUTTON_4})
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }

    private Keyboard Menu0_2() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(new KeyboardButton(MENU_1_2_BUTTON_1))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_2))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_3))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_4))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_5))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_6))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_7))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_8))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_9))
                .addRow(new KeyboardButton(MENU_1_BUTTON_3), new KeyboardButton(MENU_1_BUTTON_4))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }
}