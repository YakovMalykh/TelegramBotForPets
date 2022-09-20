package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.CheckService;
import sky.pro.telegrambotforpets.model.Adopter;
import sky.pro.telegrambotforpets.model.CatAdopter;
import sky.pro.telegrambotforpets.model.DogAdopter;
import sky.pro.telegrambotforpets.model.Report;
import sky.pro.telegrambotforpets.repositories.ReportRepository;

import static sky.pro.telegrambotforpets.constants.Coment.*;

@Service
public class CheckServiceImpl implements CheckService {

    private final Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);

    private final TelegramBot bot;
    private final ReportRepository reportRepository;
    private final AdopterServiceImpl adopterService;

    public CheckServiceImpl(TelegramBot bot, ReportRepository reportRepository, AdopterServiceImpl adopterService) {
        this.bot = bot;
        this.reportRepository = reportRepository;
        this.adopterService = adopterService;
    }

    /**
     * отправляет в чат усыновителю предупреждение и коммент волонтера по отчету
     *
     * @param chatId
     * @param volunteersComment
     */
    @Override
    public void reportIsPoorlyCompleted(Long chatId, String volunteersComment) {
        bot.execute(new SendMessage(chatId, NOTICE));
        bot.execute(new SendMessage(chatId, volunteersComment));
        logger.info("усыновителю отправлдено предупреждение");
    }


    /**
     * проверяет поля отчета и если поле не заполнено - высылает усыновителю сообщение, что его нужно заполнить
     *
     * @param report
     */
    @Override// может его нужно сделать приватным и вызывать его из метода. которы ежедневно проверяет наличие отчета?
    public void checksAllFieldsAreFilled(Report report) {
        if (report != null) {
            Long chatId = getChatId(report);
            if (chatId != null) {
                String photoPath = report.getPhotoPath();
                String ration = report.getRation();
                String behaivor = report.getBehaivor();
                String feeling = report.getFeeling();

                if (photoPath == null || photoPath.isEmpty()) {
                    logger.info("метод checksAllFieldsAreFilled - просит дослать фото");
                    bot.execute(new SendMessage(chatId, "дошлите фото животного в отчет за " + report.getDate()));
                }
                if (ration == null || ration.isEmpty()) {
                    logger.info("метод checksAllFieldsAreFilled - просит дослать рацион");
                    bot.execute(new SendMessage(chatId, "дошлите рацион животного в отчет за " + report.getDate()));
                }
                if (behaivor == null || behaivor.isEmpty()) {
                    logger.info("метод checksAllFieldsAreFilled - просит дослать описание поведения");
                    bot.execute(new SendMessage(chatId, "дошлите описание поведения животного в отчет за " + report.getDate()));
                }
                if (feeling == null || feeling.isEmpty()) {
                    logger.info("метод checksAllFieldsAreFilled - просит дослать описание самочувствия");
                    bot.execute(new SendMessage(chatId, "дошлите описание самочувствия животного в отчет за " + report.getDate()));
                }
            }
        }
    }

    private Long getChatId(Report report) {
        Long adopterId = report.getAdoption().getAdopterId();
        KindOfAnimal kindOfAnimal = KindOfAnimal.valueOf(report.getAdoption().getKindOfAnimal());
        Adopter adopter = adopterService.getAdopterById(adopterId, kindOfAnimal);
        if (adopter != null) {
            switch (kindOfAnimal) {
                case DOGS -> {
                    DogAdopter dogAdopter = (DogAdopter) adopter;
                    return dogAdopter.getChatId();
                }
                case CATS -> {
                    CatAdopter catAdopter = (CatAdopter) adopter;
                    return catAdopter.getChatId();
                }
            }
        }
        return null;

    }

}
