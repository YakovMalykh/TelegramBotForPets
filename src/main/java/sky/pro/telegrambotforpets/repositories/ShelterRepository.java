package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter,Long> {
}
