package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Report;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository extends JpaRepository <Report,Long> {
    Optional<Report> findReportById(Long id);

    Optional<Report> findReportByDateAndAdaptationId(LocalDate date, Long id);
}
