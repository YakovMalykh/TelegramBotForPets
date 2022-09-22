package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.AdoptionsResult;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.CheckService;
import sky.pro.telegrambotforpets.model.*;
import sky.pro.telegrambotforpets.repositories.AdoptionRepository;
import sky.pro.telegrambotforpets.repositories.ReportRepository;
import sky.pro.telegrambotforpets.repositories.VolunteerRepository;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static sky.pro.telegrambotforpets.constants.AdoptionsResult.*;
import static sky.pro.telegrambotforpets.constants.Coment.*;

@Service
@Transactional
public class CheckServiceImpl implements CheckService {

    private final Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);

    private final TelegramBot bot;
    private final ReportRepository reportRepository;
    private final AdopterServiceImpl adopterService;
    private final AdoptionRepository adoptionRepository;
    private final VolunteerRepository volunteerRepository;

    public CheckServiceImpl(TelegramBot bot, ReportRepository reportRepository, AdopterServiceImpl adopterService, AdoptionRepository adoptionRepository, VolunteerRepository volunteerRepository) {
        this.bot = bot;
        this.reportRepository = reportRepository;
        this.adopterService = adopterService;
        this.adoptionRepository = adoptionRepository;
        this.volunteerRepository = volunteerRepository;
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
    private void checksAllFieldsAreFilled(Report report) {
        if (report != null) {
            Long chatId = getChatId(report.getAdoption());
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

    /**
     * ищет по записи об усыновлении chatID усыновителя
     *
     * @param adoption
     * @return
     */
    private Long getChatId(Adoption adoption) {
        Long adopterId = adoption.getAdopterId();
        KindOfAnimal kindOfAnimal = KindOfAnimal.valueOf(adoption.getKindOfAnimal());
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

    /**
     * отправляет усыновителю сообщение с результатом испытательного срока
     *
     * @param adoption
     * @param adoptionsResult
     */
    @Override
    public void notifications(Adoption adoption, AdoptionsResult adoptionsResult) {
        Long chatId = getChatId(adoption);
        switch (adoptionsResult) {
            case SUCCESS -> {
                bot.execute(new SendMessage(chatId, "Поздравляем, вы прошли испытательный срок"));
                logger.info("метод notifications - успех");
            }
            case EXTENSION_14 -> {
                bot.execute(new SendMessage(chatId, "Испытательный срок продлен на 14 дней, " +
                        "продолжайте присылать отчеты"));
                logger.info("метод notifications - проделние на 14 дней");
            }
            case EXTENSION_30 -> {
                bot.execute(new SendMessage(chatId, "Испытательный срок продлен на 30 дней, " +
                        "продолжайте присылать отчеты"));
                logger.info("метод notifications - проделние на 30 дней");
            }
            case FAIL -> {
                bot.execute(new SendMessage(chatId, "Вы не прошли испытательный срок. Вам нужно вернуть питомца."));
                logger.info("метод notifications - провал");
            }
        }
    }

    /**
     * возвращает список усыновлений на испытательном сроке, включая продленные
     * @return
     */
    private List<Adoption> listWithTrialPeriod() {//здесь метод падает с ошибкой, елси в поле результат нет
        // даже  Null. Такое возможно только если руками в БД зайти и очистить поле.
        return adoptionRepository.findAll().stream().filter(e ->
                valueOf(e.getAdoptionsResult()) != SUCCESS && valueOf(e.getAdoptionsResult()) != FAIL).toList();
    }

    @Override
    @Scheduled(cron = "0 0 09 * * *")//будет выполняться каддый день в 9:00
    public void dailyCheck() {
        // получаем список всех усыновлений на испытательном сроке, включаю продленные
        List<Adoption> adoptions = listWithTrialPeriod();

        //нам интересен вчерашний день
        LocalDate yesterday = LocalDate.now().minusDays(1);

        //проходим циклом по всем полученным усыновлениям
        for (int i = 0; i < adoptions.size(); i++) {
            // получаем каждое усыновление из списка
            Adoption certainAdoption = adoptions.get(i);
            // проверяем есть ли у конкретного усыновления отчет с вчерашней датой
            Optional<Report> reportByYesterday = reportRepository.findByAdoptionAndDate(certainAdoption, yesterday);

            // если отчет с вчерашней датой есть
            if (reportByYesterday.isPresent()) {
                // проверяем что у него заполнены все поля
                logger.info("метод dailyCheck - отчет за вчерашний день есть, " +
                        "прошла проверка заполнены ли все поля отчета");
                checksAllFieldsAreFilled(reportByYesterday.get());
            } else {//если отчета с вчерашней датой нет, получаем chatID из усыновления
                Long chatIdInThisAdoption = getChatId(certainAdoption);
                // оправляем напоминание в чат усыновителю
                bot.execute(new SendMessage(chatIdInThisAdoption, "Напоминаем о необходимости ежедневно " +
                        "высылать отчеты до истечения испытательного срока"));
                logger.info("метод dailyCheck - отчета за вчерашний день нет - отправлено напоминание усыновителю");
                // и проверяем, а был ли отчет за позавчера
                Optional<Report> isReportForDayBeforeYesterdayPresented =
                        reportRepository.findByAdoptionAndDate(certainAdoption, yesterday.minusDays(1));
                // если отчета за позавчера нет, то рассылаем всем волонтерам в чаты сообщение об этом
                if (isReportForDayBeforeYesterdayPresented.isEmpty() &&
                        certainAdoption.getAdoptionsDate().isBefore(yesterday)) {
                    logger.info("метод dailyCheck - нет отчета за 2 дня - разослано уведомление волонтерам в чаты" );
                    volunteerRepository.findAll().stream().forEach(
                            v -> bot.execute(new SendMessage(v.getVolunteerChatId(),
                                    "не присылался отчет уже 2 дня по записи об усыновлении " + certainAdoption))
                    );
                }
            }
        }
    }


}
