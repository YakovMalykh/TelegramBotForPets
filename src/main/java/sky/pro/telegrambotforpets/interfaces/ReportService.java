package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.File;
import sky.pro.telegrambotforpets.constants.ReportFild;

import java.io.IOException;

public interface ReportService {
    boolean updateReportToDB(Long adoptationId);
    boolean saveReporFildToDB(ReportFild reportFild, Long adoptationId, String text) ;
    boolean saveReportFotoFildToDB( Long adoptationId, File photo) throws IOException;
}
