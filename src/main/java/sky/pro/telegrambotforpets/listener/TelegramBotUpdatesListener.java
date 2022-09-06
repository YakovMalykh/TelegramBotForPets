package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
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
    private final sky.pro.telegrambotforpets.menu.ReplyKeyboard replyKeyboard;
    private final GuestRepository guestRepository;

    public TelegramBotUpdatesListener(GuestServiceImpl guestService, sky.pro.telegrambotforpets.menu.ReplyKeyboard replyKeyboard,GuestRepository guestRepository) {
        this.guestService = guestService;
        this.replyKeyboard = replyKeyboard;
        this.guestRepository=guestRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message().chat().id() == null) {
                try {
                    throw new NoSuchMethodException();
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Long chatId = update.message().chat().id();
                if (!guestService.doesGuestAlreadyExistsInDB(update)) {
                    guestService.saveGuestToDB(update);
                }
                if (!(update.message().text() == null)) {
                    switch (update.message().text()) {
                        case MENU_1_BUTTON_1 -> {
                            sendMsg(chatId, "Информация о приюте");
                            sendMenu(replyKeyboard.Menu0_1(), chatId);
                        }
                        case MENU_1_1_BUTTON_1 -> sendMsg(chatId, "Здесь будем выдавать описание приюта");
                        case MENU_1_1_BUTTON_2 -> sendMsg(chatId, "Расписание и адрес приюта");
                        case MENU_1_1_BUTTON_3 -> sendMsg(chatId, "Техника безопасности на территории приюта");
                        case MENU_1_1_BUTTON_4 -> {
                            sendMsg(chatId, "Оставить контакт");
                        }
                        case MENU_1_BUTTON_2 -> {
                            //Здесь будет ответ на запрос Как взять питомца
                            sendMsg(chatId, "Как взять питомца из приюта");
                            sendMenu(replyKeyboard.Menu0_2(), chatId);
                        }
                        case MENU_1_2_BUTTON_1 -> sendMsg(chatId, "Правила знакомства с питомцем");
                        case MENU_1_2_BUTTON_2 -> sendMsg(chatId, "Документы необходимые, чтобы взять питомца");
                        case MENU_1_2_BUTTON_3 -> sendMsg(chatId, "Рекомендации по транспортировке питомца");
                        case MENU_1_2_BUTTON_4 -> sendMsg(chatId, "Рекомендации по обустройству дома для щенка");
                        case MENU_1_2_BUTTON_5 ->
                                sendMsg(chatId, "Рекомендации по обустройству дома для взрослой собаки");
                        case MENU_1_2_BUTTON_6 ->
                                sendMsg(chatId, "Рекомендации по обустройству дома для собаки с ограниченными возможностями");
                        case MENU_1_2_BUTTON_7 -> sendMsg(chatId, "Советы кинолога по первичному общению с питомцем");
                        case MENU_1_2_BUTTON_8 -> sendMsg(chatId, "Список проверенных кинологов");
                        case MENU_1_2_BUTTON_9 -> sendMsg(chatId, "Причины отказа в заборе питомца");
                        case MENU_1_BUTTON_3 -> {
                            //Здесь будет Отправка отчета
                            sendMsg(chatId, "Отправить отчет о питомце");
                        }
                        case MENU_1_BUTTON_4 -> {
                            //Здесь будем звать волонтера
                            sendMsg(chatId, "Позвать волонтера");
                        }
                        default -> {
                            //В ответ на неопознанную команду выдает меню
                            logger.info("Неккоректная команда");
                            sendMenu(replyKeyboard.Menu0(), chatId);
                        }
                    }
                }
            }
            if (!(update.message().contact() == null)) {
                Guest guest=new Guest();
                guest.setUserName(update.message().contact().firstName());
                guest.setChatId(update.message().contact().userId());
                guest.setPhoneNumber(update.message().contact().phoneNumber());
                guestRepository.save(guest);
                logger.info("Данные о госте занесены в БД");
                System.out.println(update.message().contact());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void sendMsg(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse responce = telegramBot.execute(message);
        System.out.println(responce.isOk());
        System.out.println(responce.errorCode());

    }

    private void sendMenu(Keyboard Menu, long chatId) {
        SendMessage request = new SendMessage(chatId, "Выберите пункт меню")
                .replyMarkup(Menu)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBot.execute(request);
        logger.info("Меню отправлено" + Menu);
        if (!sendResponse.isOk()) {
            int codeError = sendResponse.errorCode();
            String description = sendResponse.description();

        }

    }


}