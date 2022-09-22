package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.pro.telegrambotforpets.model.Adoption;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption,Long> {


List<Optional<Adoption>> findAdoptionByAdopterId(Long adopterId);
 Optional<Adoption> findFirstByAdopterIdAndKindOfAnimal(Long adopterId, String kindOfAnimal);

}
