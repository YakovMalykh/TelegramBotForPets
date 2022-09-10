package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Dog;

public interface DogRepository extends JpaRepository<Dog,Long> {
}
