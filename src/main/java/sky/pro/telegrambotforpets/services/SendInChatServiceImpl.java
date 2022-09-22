package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Buttons;
import sky.pro.telegrambotforpets.constants.Descriptions;
import sky.pro.telegrambotforpets.interfaces.AdopterService;
import sky.pro.telegrambotforpets.interfaces.SendInChatService;
import sky.pro.telegrambotforpets.listener.TelegramBotUpdatesListener;
import sky.pro.telegrambotforpets.menu.InlineKeyboard;
import sky.pro.telegrambotforpets.model.DocumentsForPreparation;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.DocumentsForPreparationRepository;
import sky.pro.telegrambotforpets.repositories.ShelterRepository;

import java.io.File;

import static sky.pro.telegrambotforpets.constants.Descriptions.*;


@Service
public class SendInChatServiceImpl implements SendInChatService {
    @Autowired
    private TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final DocumentsForPreparationRepository documentsForPreparationRepository;
    private final ShelterRepository shelterRepository;
    private final InlineKeyboard inlineKeyboard;

    private final AdopterService adopterService;

    public SendInChatServiceImpl(DocumentsForPreparationRepository documentsForPreparationRepository, ShelterRepository shelterRepository, InlineKeyboard inlineKeyboard, AdopterService adopterService) {
        this.documentsForPreparationRepository = documentsForPreparationRepository;
        this.shelterRepository = shelterRepository;
        this.inlineKeyboard = inlineKeyboard;
        this.adopterService = adopterService;
    }


    public String chouseMenu(Buttons button, Long chatId, Long shelterId) {
        logger.info("Button" + button + " Chatid" + chatId + " Shelterid" + shelterId);
        if (shelterId == null || chatId == null) {
            throw new NullPointerException("Такого приюта нет");
        } else {
            Shelter shelter = shelterRepository.findShelterById(shelterId);
            switch (button) {
                case MENU_1_BUTTON_1 -> {
                    sendMsg(chatId, "Информация о приюте");
                    sendMenu(chatId, inlineKeyboard.MenuAboutShelter(shelterId));
                }
                case MENU_1_1_BUTTON_1 -> {
                    sendMsg(chatId, shelter.getDescription());
                }
                case MENU_1_1_BUTTON_2 -> {
                    sendMsg(chatId, shelter.getSchedule());
                }
                case MENU_1_1_BUTTON_3 -> {
                    sendMsg(chatId, shelter.getAdress());
                    sendDoc(chatId, new File(shelter.getMapPath()));
                }
                case MENU_1_1_BUTTON_4 -> {
                    sendDoc(chatId, new File(shelter.getRecomendationPath()));
                }
                case MENU_1_1_BUTTON_5 -> {
                    sendMsg(chatId, shelter.getSecurityPhoneNumber());
                }

                case MENU_1_BUTTON_5 -> {
                    sendMsg(chatId, "Здась будем обрабатывать контакт");
                }
                case MENU_1_BUTTON_2 -> {
                    sendMsg(chatId, "Как взять питомца из приюта");
                    sendMenu(chatId, inlineKeyboard.MenuHowGetPet(shelter));
                }
                case MENU_1_BUTTON_3->{
                    sendMsg(chatId, "Отправьте отчет по питомцу");
                    sendMenu(chatId, inlineKeyboard.MenuReport(shelterId));
                }
                case MENU_1_2_BUTTON_1 -> {
                    sendMsg(chatId, "Правила знакомства с питомцем");
                    sendDocumentsForPreparation(chatId, DOG_DAITING_RULES, shelter);
                }
                case MENU_1_2_BUTTON_2 -> {
                    sendMsg(chatId, "Документы необходимые, чтобы взять питомца");
                    sendDocumentsForPreparation(chatId, LIST_DOCUMENTS_FOR_ADOPTING, shelter);
                }
                case MENU_1_2_BUTTON_3 -> {
                    sendMsg(chatId, "Рекомендации по транспортировке питомца");
                    sendDocumentsForPreparation(chatId, TRANSPORTATION_ADVICE, shelter);
                }
                case MENU_1_2_BUTTON_4 -> {
                    sendMsg(chatId, "Рекомендации по обустройству дома для щенка");
                    sendDocumentsForPreparation(chatId, PREPARING_HOUSE_FOR_A_PUPPY, shelter);
                }
                case MENU_1_2_BUTTON_5 -> {
                    sendMsg(chatId, "Рекомендации по обустройству дома для взрослой собаки");
                    sendDocumentsForPreparation(chatId, PREPARING_HOUSE_FOR_AN_ADULT_DOG, shelter);
                }
                case MENU_1_2_BUTTON_6 -> {
                    sendMsg(chatId, "Рекомендации по обустройству дома для собаки с ограниченными возможностями");
                    sendDocumentsForPreparation(chatId, PREPARING_HOUSE_FOR_A_DISABLED_DOG, shelter);
                }
                case MENU_1_2_BUTTON_7 -> {
                    sendMsg(chatId, "Советы кинолога по первичному общению с питомцем");
                    sendDocumentsForPreparation(chatId, DOGHANDLER_ADVICIES, shelter);
                }
                case MENU_1_2_BUTTON_8 -> {
                    sendMsg(chatId, "Список проверенных кинологов");
                }
                case MENU_1_2_BUTTON_9 -> {
                    sendMsg(chatId, "Причины отказа в заборе питомца");
                    sendDocumentsForPreparation(chatId, REASONS_FOR_REFUSAL, shelter);
                }

                default -> {
                    //В ответ на неопознанную команду выдает меню
                    logger.info("Неккоректная команда");
                    sendMenu(chatId, inlineKeyboard.Menu());
                }
            }
        }
        return "Что то пошло не так";
    }


    public void sendDocumentsForPreparation(Long chatId, Descriptions descriptionFile, Shelter shelter) {
        DocumentsForPreparation fileDoc = documentsForPreparationRepository.findDocumentsForPreparationByDescriptionAndKindOfAnimal(descriptionFile.toString(),shelter.getSpecialization());
        if (fileDoc != null) {
            File sendFile = new File(fileDoc.getFilePath());
            sendDoc(chatId, sendFile);
        } else {
            sendMsg(chatId, "Документ в работе, попробуйте запросить попозже");
            logger.info("Документ отсутсвует в БД ");
        }

    }

    public void sendDoc(Long chatId, File sendFile) {
        SendDocument document = new SendDocument(chatId, sendFile);
        SendResponse sendResponse = telegramBot.execute(document);
        logger.info("Документ отправлен " + sendFile.getName());
        if (!sendResponse.isOk()) {
            int codeError = sendResponse.errorCode();
            String description = sendResponse.description();
        }
    }

    public void sendMsg(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse responce = telegramBot.execute(message);
        System.out.println(responce.isOk());
        System.out.println(responce.errorCode());

    }

    public void sendMenu(Long chatId, Keyboard Menu) {
        String nameOfPerson = adopterService.greeting(chatId);
        SendMessage request = new SendMessage(chatId, nameOfPerson + ", выберите пункт меню")
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