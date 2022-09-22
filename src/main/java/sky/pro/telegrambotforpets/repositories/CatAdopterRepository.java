package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Adopter;
import sky.pro.telegrambotforpets.model.CatAdopter;

import java.util.Optional;

public interface CatAdopterRepository extends JpaRepository<CatAdopter,Long> {

    Optional<CatAdopter> findByPhoneNumber (String phoneNumber);

    Optional<CatAdopter> findByChatId(Long chatId);

    Optional<Adopter> getCatAdopterByChatId(Long chatId);
}
