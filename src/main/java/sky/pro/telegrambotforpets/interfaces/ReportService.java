package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.Document;
import sky.pro.telegrambotforpets.constants.ReportFild;
import sky.pro.telegrambotforpets.model.Report;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportService {
    // boolean createReportToDB(Long adoptionId);
    String saveReporFildToDB(ReportFild reportFild, Long adoptionId, String text);

   // boolean saveReportFotoFildToDB(Long adoptionId, List<PhotoSize> photos) throws IOException;

    String saveReportFotoFildToDB(Long adoptionId, Document photos, Long chatId) throws IOException;

    List<Report> findAll(Integer pageNumber, Integer pageSize);

    List<Report> findByAdoptionId(Long adoptionId);

    Optional<Report> findByDateAndAdoptionId(LocalDate date, Long adoptionId);
}
