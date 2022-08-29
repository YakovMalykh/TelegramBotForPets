package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Guest;

public interface GuestRepository extends JpaRepository <Guest,Long> {

}
