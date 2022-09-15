package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Buttons;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.interfaces.SendInChatService;
import sky.pro.telegrambotforpets.menu.InlineKeyboard;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.DocumentsForPreparationRepository;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.repositories.ShelterRepository;
import sky.pro.telegrambotforpets.services.CallVolunteerService;
import sky.pro.telegrambotforpets.services.GuestServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * отвечает за работу телеграм бота
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {


    @Autowired
    private TelegramBot telegramBot;

    private final GuestService guestService;
    private final SendInChatService sendInChatService;
    //  private final sky.pro.telegrambotforpets.menu.ReplyKeyboard replyKeyboard;
    private final sky.pro.telegrambotforpets.menu.InlineKeyboard inlineKeyboard;
    private final GuestRepository guestRepository;
    private final ShelterRepository shelterRepository;
    private final CallVolunteerService callVolunteerService;

    public TelegramBotUpdatesListener(GuestServiceImpl guestService, SendInChatService sendInChatService,
                                      GuestRepository guestRepository, DocumentsForPreparationRepository documentsForPreparationRepository,
                                      InlineKeyboard inlineKeyboard, ShelterRepository shelterRepository, CallVolunteerService callVolunteerService) {
        this.guestService = guestService;
        this.sendInChatService = sendInChatService;
        //  this.replyKeyboard = replyKeyboard; ReplyKeyboard replyKeyboard,
        this.guestRepository = guestRepository;
        this.inlineKeyboard = inlineKeyboard;
        this.shelterRepository = shelterRepository;
        this.callVolunteerService = callVolunteerService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            //добавил эту проверку, чтобы бот не реагировал на сообщения в групповых чатах волонтеров
//            if(!update.message().chat().id().toString().equals("-1001534654244")){

                if (update.message() != null) {
                    if (update.message().text() != null) {
                        if (!guestService.doesGuestAlreadyExistsInDB(update)) {
                            guestService.saveGuestToDB(update);
                        }
                        logger.info("update " + update.message().text());
                        sendInChatService.sendMenu(update.message().chat().id(), inlineKeyboard.Menu());
                    }
                    if (!(update.message().contact() == null)) {
                        Guest guest = new Guest();
                        guest.setUserName(update.message().contact().firstName());
                        guest.setChatId(update.message().contact().userId());
                        guest.setPhoneNumber(update.message().contact().phoneNumber());
                        guestRepository.save(guest);
                        logger.info("Данные о госте занесены в БД");
                    }
                } else if (update.callbackQuery() != null) {
                    Long chatId = update.callbackQuery().message().chat().id();
                    if (update.callbackQuery().data().split("/").length > 1) {
                        String str = update.callbackQuery().data();
                        String[] part = str.split("/");
                        Buttons button = Buttons.valueOf(part[1]);
                        Long shelterId = Long.valueOf(part[0]);
                        logger.info(shelterId + " " + button);
                        switch (button) {
                            case MENU_1_BUTTON_1, MENU_1_BUTTON_2, MENU_1_BUTTON_3,
//                                MENU_1_BUTTON_4,
                                    MENU_1_1_BUTTON_1, MENU_1_1_BUTTON_2, MENU_1_1_BUTTON_3, MENU_1_1_BUTTON_4, MENU_1_1_BUTTON_5 -> {
                                sendInChatService.chouseMenu(button, chatId, shelterId);
                            }
                            case MENU_1_BUTTON_4 -> {
                                callVolunteerService.callVolunteer(chatId, update);
                            }
/*доработать после того как в таблицу document_for_preparation будет добавлен столбец с приютом
                        case MENU_1_2_BUTTON_1, MENU_1_2_BUTTON_2, MENU_1_2_BUTTON_3, MENU_1_2_BUTTON_4, MENU_1_2_BUTTON_5, MENU_1_2_BUTTON_6, MENU_1_2_BUTTON_7, MENU_1_2_BUTTON_8, MENU_1_2_BUTTON_9 -> {
                            sendInChatService.chouseMenu(button, chatId, shelterId);
                        }

 */
                            default -> sendInChatService.sendMsg(chatId, "Некорректная команда");
                        }

                    } else {

                        switch (Buttons.valueOf(update.callbackQuery().data())) {
                            case MENU_0_BUTTON_1 -> {
                                logger.info("выбран приют для собак");
                                Shelter shelter = shelterRepository.findShelterBySpecialization("DOGS");
                                if (shelter != null) {
                                    sendInChatService.sendMenu(chatId, inlineKeyboard.MenuCommon(shelter.getId()));
                                } else {
                                    sendInChatService.sendMsg(chatId, "Такого приюта еще нет в базе данных, попробуйте выбрать другой");
                                    sendInChatService.sendMenu(chatId, inlineKeyboard.Menu());
                                }
                            }
                            case MENU_0_BUTTON_2 -> {
                                logger.info("выбран приют для кошек");
                                Shelter shelter = shelterRepository.findShelterBySpecialization("CATS");
                                if (shelter != null) {
                                    sendInChatService.sendMenu(chatId, inlineKeyboard.MenuCommon(shelter.getId()));
                                } else {
                                    sendInChatService.sendMsg(chatId, "Такого приюта еще нет в базе данных, попробуйте выбрать другой");
                                    sendInChatService.sendMenu(chatId, inlineKeyboard.Menu());
                                }
                            }
                        }
                    }
                }
//            }

        });
        logger.info("апдейт конфирм");
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


}