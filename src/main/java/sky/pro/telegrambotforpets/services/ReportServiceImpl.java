package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.ReportFild;
import sky.pro.telegrambotforpets.interfaces.ReportService;
import sky.pro.telegrambotforpets.model.Report;
import sky.pro.telegrambotforpets.repositories.ReportRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
    @Value("${path.to.reports.folder}")
    private String photoPath;
    private final ReportRepository reportRepository;
    //пока не сделана сущность усыновление
    private final Long adoptationId = 1L;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public boolean updateReportToDB(Long adoptationId) {
        if (adoptationId != null) {
            LocalDate date = LocalDate.now();
            Optional<Report> report = reportRepository.findReportByDateAndAdaptationId(date, adoptationId);
            if (!report.isPresent()) {
                Report reportNew = new Report();
                reportNew.setAdaptationId(adoptationId);
                reportNew.setDate(date);
                reportRepository.save(reportNew);
                logger.info("Отчет занесен");
                return false;
            } else {
                logger.info("Такой отчет уже есть");
                return false;
            }
        } else {
            logger.info("Такое усыновление не найдено в БД");
            return false;
        }
    }

    public boolean saveReporFildToDB(ReportFild reportFild, Long adoptationId, String text) {
        LocalDate date = LocalDate.now();
        Report report = reportRepository.findReportByDateAndAdaptationId(date, adoptationId).orElse(new Report());
        report.setAdaptationId(adoptationId);
        report.setDate(date);
        switch (reportFild) {
            case BEHAIVOR -> report.setBehaivor(text);
            case FEELING -> report.setFeeling(text);
            case RASION -> report.setRation(text);
        }
        reportRepository.save(report);
        logger.info("Поле отчета обновлено");
        return true;
    }


    public boolean saveReportFotoFildToDB(Long adoptationId, File photo) throws IOException {
        /*
        LocalDate date = LocalDate.now();
        Report report = reportRepository.findReportByDateAndAdaptationId(date, adoptationId).orElse(new Report());
        report.setAdaptationId(adoptationId);
        report.setDate(date);
        Path filePath = Path.of(photoPath, adoptationId.toString(), date.toString(), photo.filePath());
        Files.createDirectories(filePath.getParent());
        //Files.deleteIfExists(filePath);
/*
            try (
                    InputStream is = photo..getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }


        report.setPhotoPath(filePath.toString());

        //   reportRepository.save(photo);
        logger.info("вызван метода saveDocumentToDB, файл сохранен в БД");
        return true;
        */
        return false;
    }


}