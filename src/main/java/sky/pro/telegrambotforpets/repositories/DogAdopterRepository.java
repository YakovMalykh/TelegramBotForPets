package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Adopter;
import sky.pro.telegrambotforpets.model.DogAdopter;

import java.util.Optional;

public interface DogAdopterRepository extends JpaRepository<DogAdopter, Long> {

    Optional<DogAdopter> findByPhoneNumber(String phoneNumber);

    Optional<DogAdopter> findByChatId(long chatId);

    Optional<Adopter> getDogAdopterByChatId(Long chatId);
}
