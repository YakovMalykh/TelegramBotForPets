package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Adoption;

import java.time.LocalDate;
import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    List<Adoption> findByAdoptionsDateOrAdoptionsDateOrAdoptionsDate(LocalDate period30days, LocalDate period44days,
                                                                     LocalDate period60days);
}
