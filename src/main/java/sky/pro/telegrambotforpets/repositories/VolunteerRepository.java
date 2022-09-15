package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

}
