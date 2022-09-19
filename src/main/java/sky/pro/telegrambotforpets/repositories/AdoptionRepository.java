package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Adoption;

public interface AdoptionRepository extends JpaRepository<Adoption,Long> {


}
