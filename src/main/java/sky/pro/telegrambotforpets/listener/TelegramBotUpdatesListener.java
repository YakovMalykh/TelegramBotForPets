package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.interfaces.SendInChatService;
import sky.pro.telegrambotforpets.menu.ReplyKeyboard;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.repositories.DocumentsForPreparationRepository;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.services.GuestServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

import static sky.pro.telegrambotforpets.constants.Constants.*;
import static sky.pro.telegrambotforpets.constants.Descriptions.*;

/**
 * отвечает за работу телеграм бота
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {


    @Autowired
    private TelegramBot telegramBot;

    private final GuestService guestService;
    private final SendInChatService sendInChatService;
    private final sky.pro.telegrambotforpets.menu.ReplyKeyboard replyKeyboard;
    private final GuestRepository guestRepository;


    public TelegramBotUpdatesListener(GuestServiceImpl guestService, SendInChatService sendInChatService, ReplyKeyboard replyKeyboard, GuestRepository guestRepository, DocumentsForPreparationRepository documentsForPreparationRepository) {
        this.guestService = guestService;
        this.sendInChatService = sendInChatService;
        this.replyKeyboard = replyKeyboard;
        this.guestRepository = guestRepository;

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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
                            sendInChatService.sendMsg(chatId, "Информация о приюте");
                            sendInChatService.sendMenu( chatId,replyKeyboard.Menu0_1());
                        }
                        case MENU_1_1_BUTTON_1 ->
                                sendInChatService.sendMsg(chatId, "Здесь будем выдавать описание приюта");
                        case MENU_1_1_BUTTON_2 -> sendInChatService.sendMsg(chatId, "Расписание и адрес приюта");
                        case MENU_1_1_BUTTON_3 ->
                                sendInChatService.sendMsg(chatId, "Техника безопасности на территории приюта");
                        case MENU_1_1_BUTTON_4 -> {
                            sendInChatService.sendMsg(chatId, "Оставить контакт");
                        }
                        case MENU_1_BUTTON_2 -> {
                            //Здесь будет ответ на запрос Как взять питомца
                            sendInChatService.sendMsg(chatId, "Как взять питомца из приюта");
                            sendInChatService.sendMenu( chatId, replyKeyboard.Menu0_2());
                        }
                        case MENU_1_2_BUTTON_1 -> {
                            sendInChatService.sendMsg(chatId, "Правила знакомства с питомцем");
                            sendInChatService.sendDoc(chatId, DOG_DAITING_RULES);
                        }
                        case MENU_1_2_BUTTON_2 -> {
                            sendInChatService.sendMsg(chatId, "Документы необходимые, чтобы взять питомца");
                            sendInChatService.sendDoc(chatId, LIST_DOCUMENTS_FOR_ADOPTING);
                        }
                        case MENU_1_2_BUTTON_3 -> {
                            sendInChatService.sendMsg(chatId, "Рекомендации по транспортировке питомца");
                            sendInChatService.sendDoc(chatId, TRANSPORTATION_ADVICE);
                        }
                        case MENU_1_2_BUTTON_4 -> {
                            sendInChatService.sendMsg(chatId, "Рекомендации по обустройству дома для щенка");
                            sendInChatService.sendDoc(chatId, PREPARING_HOUSE_FOR_A_PUPPY);
                        }
                        case MENU_1_2_BUTTON_5 -> {
                            sendInChatService.sendMsg(chatId, "Рекомендации по обустройству дома для взрослой собаки");
                            sendInChatService.sendDoc(chatId, PREPARING_HOUSE_FOR_AN_ADULT_DOG);
                        }
                        case MENU_1_2_BUTTON_6 -> {
                            sendInChatService.sendMsg(chatId, "Рекомендации по обустройству дома для собаки с ограниченными возможностями");
                            sendInChatService.sendDoc(chatId, PREPARING_HOUSE_FOR_A_DISABLED_DOG);
                        }
                        case MENU_1_2_BUTTON_7 -> {
                            sendInChatService.sendMsg(chatId, "Советы кинолога по первичному общению с питомцем");
                            sendInChatService.sendDoc(chatId, DOGHANDLER_ADVICIES);
                        }
                        case MENU_1_2_BUTTON_8 -> {
                            sendInChatService.sendMsg(chatId, "Список проверенных кинологов");

                        }
                        case MENU_1_2_BUTTON_9 -> {
                            sendInChatService.sendMsg(chatId, "Причины отказа в заборе питомца");
                            sendInChatService.sendDoc(chatId, REASONS_FOR_REFUSAL);
                        }
                        case MENU_1_BUTTON_3 -> {
                            //Здесь будет Отправка отчета
                            sendInChatService.sendMsg(chatId, "Отправить отчет о питомце");
                        }
                        case MENU_1_BUTTON_4 -> {
                            //Здесь будем звать волонтера
                            sendInChatService.sendMsg(chatId, "Позвать волонтера");
                        }
                        default -> {
                            //В ответ на неопознанную команду выдает меню
                            logger.info("Неккоректная команда");
                            sendInChatService.sendMenu(chatId, replyKeyboard.Menu0());
                        }
                    }
                }
            }
            if (!(update.message().contact() == null)) {
                Guest guest = new Guest();
                guest.setUserName(update.message().contact().firstName());
                guest.setChatId(update.message().contact().userId());
                guest.setPhoneNumber(update.message().contact().phoneNumber());
                guestRepository.save(guest);
                logger.info("Данные о госте занесены в БД");
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}