package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sky.pro.telegrambotforpets.model.Adoption;
import sky.pro.telegrambotforpets.model.Report;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findReportById(Long id);

    /**
     * вернет список отчетов по усыновлениям на испытательном сроке
     *
     * @return List
     */
    @Query(value = "select reports.* from adoption inner join reports on adoption.id = reports.adoption_id " +
            "where adoptions_result = 'EXTENSION_30' or adoptions_result = 'EXTENSION_14' " +
            "or adoptions_result = '' or adoptions_result is null", nativeQuery = true)
    List<Report> findByTrialPeriod();

    /**
     * ищем по усыновлению и по дате отчет
     *
     * @param adoption
     * @param date
     * @return
     */
    Optional<Report> findByAdoptionAndDate(Adoption adoption, LocalDate date);


    List<Report> findReportByAdoptionId (Long id);
    Optional<Report> findReportByDateAndAdoptionId(LocalDate date, Long id);
}
