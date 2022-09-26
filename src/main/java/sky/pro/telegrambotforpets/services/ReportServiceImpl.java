package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.ReportFild;
import sky.pro.telegrambotforpets.interfaces.AdoptionService;
import sky.pro.telegrambotforpets.interfaces.ReportService;
import sky.pro.telegrambotforpets.model.Report;
import sky.pro.telegrambotforpets.repositories.ReportRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sky.pro.telegrambotforpets.constants.Buttons.MENU_EXIT;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
    @Value("${path.to.reports.folder}")
    private String photoPath;
    private final ReportRepository reportRepository;
    private final AdoptionService adoptionService;
    private final SendInChatServiceImpl sendInChatService;
    //пока не сделана сущность усыновление
    private final Long adoptationId = 1L;
    private final TelegramBot bot;

    public ReportServiceImpl(ReportRepository reportRepository, AdoptionService adoptionService, SendInChatServiceImpl sendInChatService, TelegramBot bot) {
        this.reportRepository = reportRepository;
        this.adoptionService = adoptionService;
        this.sendInChatService = sendInChatService;
        this.bot = bot;
    }

    public List<Report> findAll(Integer pageNumber, Integer pageSize) {
        logger.debug("metod findAll started");
        List<Report> reports = new ArrayList<Report>();
        if (pageNumber > 0) {
            PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
            reports = reportRepository.findAll(pageRequest).getContent();
        }
        return reports;
    }

    public List<Report> findByAdoptionId(Long adoptionId) {
        logger.debug("metod findByAdoptionAid started");
        return reportRepository.findReportByAdoptionId(adoptionId);

    }

    public Optional<Report> findByDateAndAdoptionId(LocalDate date, Long adoptionId) {
        logger.debug("metod findByDateAndAdoptionId started");
        return reportRepository.findReportByDateAndAdoptionId(date, adoptionId);

    }

    public String saveReporFildToDB(ReportFild reportFild, Long adoptationId, String text) {
        LocalDate date = LocalDate.now();
        Report report = reportRepository.findReportByDateAndAdoptionId(date, adoptationId).orElse(new Report());
        report.setAdoption(adoptionService.getAdoptionById(adoptationId).orElseThrow());
        report.setDate(date);
        switch (reportFild) {
            case BEHAIVOR -> report.setBehaivor(text);
            case FEELING -> report.setFeeling(text);
            case RASION -> report.setRation(text);
            case PHOTO -> report.setPhotoPath(text);
        }
        reportRepository.save(report);
        String msg="";
        if (report.getRation()==null) {msg=" Рацион";}
        if (report.getBehaivor()==null) {msg=msg + " Поведение";}
        if (report.getFeeling()==null) {msg=msg + " Самочувствие";}
        if (report.getPhotoPath()==null) {msg=msg + " Фото";}
        if (msg.equals("")){
            msg="Вы заполнили все поля отчета на сегодня, но если вы хотите поменять комментарий выберите соответсвующую кнопку";
        } else {
            msg = "Вы не заполнили следующую информацию: " +msg;
        }
        logger.info("Поле отчета обновлено");
        return msg;
    }

       public String saveReportFotoFildToDB(Long adoptationId, Document photos, Long chatId) throws IOException {
        logger.info("ad" + adoptationId);
        String msg = "";
        LocalDate date = LocalDate.now();
        Path filePath = Path.of(photoPath, String.valueOf(adoptationId), date.toString());
        Files.createDirectories(filePath);
        int photosSize = Files.list(filePath).collect(Collectors.toList()).size() + 1;
        if (photosSize <= 10) {
            String photoFildId = photos.fileId();
            String photosName = photos.fileName().toLowerCase();
            String ext = photosName.substring(photosName.lastIndexOf(".") + 1);
            logger.info(ext);
            if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
                GetFile request = new GetFile(photoFildId);
                GetFileResponse getFileResponse = bot.execute(request);
                File file = getFileResponse.file();
                file.fileId();
                file.filePath();
                file.fileSize();
                Long fileSize = file.fileSize();
                logger.info(fileSize.toString());
                if (fileSize > 5242880) {
                    msg = "Превышен максимальный размер фото, фото " + photosName + "не сохранено";
                } else {
                    String fullPath = bot.getFullFilePath(file);
                    try (InputStream in = new URL(fullPath).openStream()) {
                        Files.copy(in, Path.of(String.valueOf(filePath), photosSize + "." + ext));
                        in.close();
                        saveReporFildToDB(ReportFild.PHOTO, adoptationId, String.valueOf(Path.of(String.valueOf(filePath), photosSize + "." + ext)));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    msg = "Фото " + photosName + " сохранено под номером " + photosSize;
                }
            } else {
                msg = "Пожалуйста, отправьте фото в формате .jpg,.jpeg или .png";

            }
        } else {
            msg = "Вы сегодня уже отправили достаточно фото, нажмите кнопку " + MENU_EXIT.getButtonName();
        }
        return msg;
    }


}