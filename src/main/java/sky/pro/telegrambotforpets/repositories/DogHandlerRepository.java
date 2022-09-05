package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.DogHandler;

public interface DogHandlerRepository extends JpaRepository<DogHandler, Long> {
}
