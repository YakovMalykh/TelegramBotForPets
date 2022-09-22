package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import sky.pro.telegrambotforpets.constants.ReportFild;
import sky.pro.telegrambotforpets.model.Report;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    boolean updateReportToDB(Long adoptionId);
    boolean saveReporFildToDB(ReportFild reportFild, Long adoptionId, String text) ;
    boolean saveReportFotoFildToDB( Long adoptionId, List<PhotoSize> photos) throws IOException;
    String saveReportFotoFildToDB_(Long adoptionId, Document photos, Long chatId) throws IOException ;
    List<Report> findAll(Integer pageNumber, Integer pageSize);
    }
