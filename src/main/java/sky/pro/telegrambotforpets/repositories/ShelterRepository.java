package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.Shelter;

import java.util.Optional;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Shelter findFirstByNameIgnoreCase(String name);

    Shelter findShelterById(Long shelterId);

    /**
     * в команде условились, что пока может быть только 2 приюта,
     * иначе логику этого метода нужно менять - займемся, когда если останется время
     *
     * @param specialiZation
     * @return
     */
    Shelter findShelterBySpecialization(String specialiZation);

    Optional<Shelter> getSheltersById(Long id);


}
