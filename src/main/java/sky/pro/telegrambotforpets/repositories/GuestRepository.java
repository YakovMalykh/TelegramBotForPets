package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Guest;

import java.util.Optional;

public interface GuestRepository extends JpaRepository <Guest,Long> {

    Optional<Guest> findByPhoneNumber(String phoneNumber);

    Optional<Guest> findByChatId (Long chatId);
}
