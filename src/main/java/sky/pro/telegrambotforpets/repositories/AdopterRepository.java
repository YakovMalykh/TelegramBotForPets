package sky.pro.telegrambotforpets.repositories;

import sky.pro.telegrambotforpets.model.CatAdopter;
import sky.pro.telegrambotforpets.model.DogAdopter;

import java.util.Optional;


public class AdopterRepository {
    private final CatAdopterRepository catAdopterRepository;
    private final DogAdopterRepository dogAdopterRepository;
 //   private final JpaRepository adopterRepositiry;

    public AdopterRepository(CatAdopterRepository catAdopterRepository, DogAdopterRepository dogAdopterRepository) {
        this.catAdopterRepository = catAdopterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
    //    this.adopterRepositiry = adopterRepositiry;
    }

    Optional<CatAdopter> getCatAdopterByChatId(Long chatId) {
        Optional<CatAdopter> adopter = catAdopterRepository.findByChatId(chatId);
        return adopter;
    }
    Optional<DogAdopter> getDogAdopterByChatId(Long chatId) {
        Optional<DogAdopter> adopter = dogAdopterRepository.findByChatId(chatId);
        return adopter;
    }

}
