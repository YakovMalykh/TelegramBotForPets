package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Coment;
import sky.pro.telegrambotforpets.interfaces.CheckService;
import sky.pro.telegrambotforpets.repositories.ReportRepository;

@Service
public class CheckServiceImpl implements CheckService {

    private final Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);

    private final TelegramBot bot;
    private final ReportRepository reportRepository;

    public CheckServiceImpl(TelegramBot bot, ReportRepository reportRepository) {
        this.bot = bot;
        this.reportRepository = reportRepository;
    }

    /**
     * отправляет в чат усыновителю предупреждение и коммент волонтера по отчету
     * @param chatId
     * @param volunteersComment
     */
    @Override
    public void reportIsPoorlyCompleted(Long chatId, String volunteersComment) {
        bot.execute(new SendMessage(chatId, Coment.NOTICE));
        bot.execute(new SendMessage(chatId, volunteersComment));
        logger.info("усыновителю отправлдено предупреждение");
    }

}
