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
import sky.pro.telegrambotforpets.constants.Descriptions;
import sky.pro.telegrambotforpets.interfaces.SendInChatService;
import sky.pro.telegrambotforpets.listener.TelegramBotUpdatesListener;
import sky.pro.telegrambotforpets.model.DocumentsForPreparation;
import sky.pro.telegrambotforpets.repositories.DocumentsForPreparationRepository;

import java.io.File;

@Service
public class SendInChatServiceImpl implements SendInChatService {
    @Autowired
    private TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final DocumentsForPreparationRepository documentsForPreparationRepository;

    public SendInChatServiceImpl(DocumentsForPreparationRepository documentsForPreparationRepository) {
        this.documentsForPreparationRepository = documentsForPreparationRepository;
    }

    public void sendDoc(Long chatId, Descriptions descriptionFile) {
        DocumentsForPreparation fileDoc = documentsForPreparationRepository.findDocumentsForPreparationByDescription(descriptionFile.toString());
        if (fileDoc != null){
            File sendFile = new File(fileDoc.getFilePath());
            SendDocument document = new SendDocument(chatId, sendFile);
            SendResponse sendResponse = telegramBot.execute(document);
            logger.info("Документ отправлен " + descriptionFile);
            if (!sendResponse.isOk()) {
                int codeError = sendResponse.errorCode();
                String description = sendResponse.description();
            }
        } else {
            sendMsg(chatId, "Документ в работе, попробуйте запросить попозже");
            logger.info("Документ отсутсвует в БД ");
        }

    }


    public void sendMsg(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse responce = telegramBot.execute(message);
        System.out.println(responce.isOk());
        System.out.println(responce.errorCode());

    }

    public void sendMenu(Long chatId, Keyboard Menu ) {
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
