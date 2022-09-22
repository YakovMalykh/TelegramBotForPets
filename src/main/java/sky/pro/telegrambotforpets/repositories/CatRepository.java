package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Cat;

public interface CatRepository extends JpaRepository<Cat, Long> {

    Cat findByNameIgnoreCase(String name);

}
