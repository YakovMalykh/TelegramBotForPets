package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Buttons;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.constants.ReportFild;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.interfaces.ReportService;
import sky.pro.telegrambotforpets.interfaces.SendInChatService;
import sky.pro.telegrambotforpets.menu.InlineKeyboard;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.repositories.ShelterRepository;
import sky.pro.telegrambotforpets.services.CallVolunteerService;
import sky.pro.telegrambotforpets.services.GuestServiceImpl;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

import static sky.pro.telegrambotforpets.constants.Buttons.MENU_1_3_BUTTON_1;
import static sky.pro.telegrambotforpets.constants.Buttons.MENU_EXIT;
import static sky.pro.telegrambotforpets.constants.Coment.COMENT_FILLING_REPORT;

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
    private final ReportService reportService;

    public TelegramBotUpdatesListener(GuestServiceImpl guestService, SendInChatService sendInChatService,
                                      GuestRepository guestRepository, InlineKeyboard inlineKeyboard,
                                      ShelterRepository shelterRepository, CallVolunteerService callVolunteerService, ReportService reportService) {
        this.guestService = guestService;
        this.sendInChatService = sendInChatService;
        this.guestRepository = guestRepository;
        this.inlineKeyboard = inlineKeyboard;
        this.shelterRepository = shelterRepository;
        this.callVolunteerService = callVolunteerService;
        this.reportService = reportService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    private final Long adoptationId = 1L;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private HashMap<Long, ReportFild> updateStatus = new HashMap<>();

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            //добавил здесь проверку, чтобы бот не реагировал на сообщения в групповых чатах волонтеров
            if (update.message() != null && !callVolunteerService.isItGroupChatToTalkWithVolunteer(update)) {
                if (updateStatus.containsKey(update.message().chat().id())) {

                    logger.info("проверка статуса");
                    ReportFild reportFild = updateStatus.get(update.message().chat().id());
                    switch (updateStatus.get(update.message().chat().id())) {

                        case PHOTO -> {
                            if (update.message().photo() != null) {
/* в работе
                                long chat_id = update.message().chat().id();
                                List<PhotoSize> photos = List.of(update.message().photo());
                                String f_id = photos.stream()

                                        .findFirst()
                                        .orElse(null).fileId();

                                   String url= "https://api.telegram.org/bot"+telegramBot.getToken()+"/getFile?file_id="+f_id;

                                String caption = "file_id: " + f_id ;
                                SendPhoto msg = new SendPhoto(chat_id,f_id);
                                logger.info(url);
                                logger.info(f_id);
                                */
                                logger.info("фото");

                            } else {
                                logger.info("не фото");
                                sendInChatService.sendMsg(update.message().chat().id(), "отправьте фото или нажмите кнопку " + MENU_EXIT.getButtonName());
                            }
                        }
                        case RASION, FEELING, BEHAIVOR -> {
                            if (update.message().text() != null) {
                                logger.info("текст");
                                reportService.saveReporFildToDB(reportFild, adoptationId, update.message().text());
                                sendInChatService.sendMsg(update.message().chat().id(), "Поле " + reportFild + " отчета обновлено");
                                updateStatus.remove(update.message().chat().id());
                            } else {
                                logger.info("не текст");
                                sendInChatService.sendMsg(update.message().chat().id(), "отправьте фото или нажмите кнопку " + MENU_EXIT.getButtonName());
                            }
                        }
                    }

                } else {
                    if (update.message().text() != null) {
                        if (!guestService.doesGuestAlreadyExistsInDB(update)) {
                            telegramBot.execute(guestService.firstMeeting(update));
                            guestService.saveGuestToDB(update);
                        }
                        logger.info("update " + update.message().text());
                        sendInChatService.sendMenu(update.message().chat().id(), inlineKeyboard.Menu());
                    }
                    if (!(update.message().contact() == null)) {
                        guestService.saveContactToDB(update);
                    }

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
                        case MENU_1_BUTTON_1, MENU_1_BUTTON_2,
                                MENU_1_1_BUTTON_1, MENU_1_1_BUTTON_2, MENU_1_1_BUTTON_3, MENU_1_1_BUTTON_4, MENU_1_1_BUTTON_5,
                                MENU_1_2_BUTTON_1, MENU_1_2_BUTTON_2, MENU_1_2_BUTTON_3, MENU_1_2_BUTTON_4, MENU_1_2_BUTTON_5,
                                MENU_1_2_BUTTON_6, MENU_1_2_BUTTON_7, MENU_1_2_BUTTON_8, MENU_1_2_BUTTON_9 -> {
                            sendInChatService.chouseMenu(button, chatId, shelterId);
                        }
                        case MENU_1_BUTTON_4 -> callVolunteerService.callVolunteer(chatId);
                        case MENU_1_BUTTON_3 -> {
                            sendInChatService.sendMsg(chatId, COMENT_FILLING_REPORT);
                            sendInChatService.sendMenu(chatId, inlineKeyboard.MenuReport());
                        }
                        default -> sendInChatService.sendMsg(chatId, "Некорректная команда");
                    }

                } else {
                    switch (Buttons.valueOf(update.callbackQuery().data())) {
                        case MENU_EXIT -> {
                            updateStatus.remove(chatId);
                            sendInChatService.sendMenu(chatId, inlineKeyboard.Menu());
                        }
                        case MENU_0_BUTTON_1 -> {
                            choseShelter(chatId, Buttons.valueOf(update.callbackQuery().data()), KindOfAnimal.DOGS);
                        }
                        case MENU_0_BUTTON_2 -> {
                            choseShelter(chatId, Buttons.valueOf(update.callbackQuery().data()), KindOfAnimal.CATS);
                        }
                        case MENU_1_3_BUTTON_1 -> {
                            sendInChatService.sendMsg(chatId, "отправьте " + MENU_1_3_BUTTON_1.getButtonName() + " или нажмите кнопку " + MENU_EXIT.getButtonName());
                            updateStatus.put(chatId, ReportFild.PHOTO);
                        }
                        case MENU_1_3_BUTTON_2 -> {
                            sendInChatService.sendMsg(chatId, "отправьте " + Buttons.valueOf(update.callbackQuery().data()).getButtonName() + " или нажмите кнопку " + MENU_EXIT.getButtonName());
                            updateStatus.put(chatId, ReportFild.RASION);
                        }
                        case MENU_1_3_BUTTON_3 -> {
                            sendInChatService.sendMsg(chatId, "отправьте " + Buttons.valueOf(update.callbackQuery().data()).getButtonName() + " или нажмите кнопку " + MENU_EXIT.getButtonName());
                            updateStatus.put(chatId, ReportFild.FEELING);
                        }
                        case MENU_1_3_BUTTON_4 -> {
                            sendInChatService.sendMsg(chatId, "отправьте " + Buttons.valueOf(update.callbackQuery().data()).getButtonName() + " или нажмите кнопку " + MENU_EXIT.getButtonName());
                            updateStatus.put(chatId, ReportFild.BEHAIVOR);
                        }
                    }
                }
            }

        });
        logger.info("апдейт конфирм");
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void choseShelter(Long chatId, Buttons button, KindOfAnimal kindOfAnimal) {
        logger.info("выбран " + button.name());
        Shelter shelter = shelterRepository.findShelterBySpecialization(kindOfAnimal.name());
        if (shelter != null) {
            sendInChatService.sendMenu(chatId, inlineKeyboard.MenuCommon(shelter, chatId));
        } else {
            sendInChatService.sendMsg(chatId, "Такого приюта еще нет в базе данных, попробуйте выбрать другой");
            sendInChatService.sendMenu(chatId, inlineKeyboard.Menu());
        }
    }

}
