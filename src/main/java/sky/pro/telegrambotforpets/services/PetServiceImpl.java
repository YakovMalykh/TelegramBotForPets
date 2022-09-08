package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.PetService;
import sky.pro.telegrambotforpets.interfaces.ShelterService;
import sky.pro.telegrambotforpets.model.Cat;
import sky.pro.telegrambotforpets.model.Dog;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.CatRepository;
import sky.pro.telegrambotforpets.repositories.DogRepository;

import javax.xml.crypto.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static sky.pro.telegrambotforpets.constants.KindOfAnimal.CATS;
import static sky.pro.telegrambotforpets.constants.KindOfAnimal.DOGS;

@Service
public class PetServiceImpl implements PetService {

    private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final ShelterService shelterService;

    public PetServiceImpl(DogRepository dogRepository, CatRepository catRepository, ShelterService shelterService) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.shelterService = shelterService;
    }

    @Override
    public boolean savePetToDB(String name, String birthDay, Gender gender, Long breedId, Boolean sterilized,
                               Boolean invalid, KindOfAnimal kindOfAnimal, Long shelterId) {
        Shelter shelter = shelterService.getShelter(shelterId);
        String specialization = shelter.getSpecialization();
        LocalDate parseDate = LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        if (kindOfAnimal == DOGS) {
            if (specialization.equals(kindOfAnimal.name())) {
                Dog dog = new Dog(name, parseDate, gender.name(), breedId, sterilized, invalid,
                        kindOfAnimal.name(), shelter);
                dogRepository.save(dog);
                logger.info("метод savePetToDB - собака сохранена в БД: " + name);
                return true;
            }
        } else if (kindOfAnimal == CATS) {
            if (specialization.equals(kindOfAnimal.name())) {
                Cat cat = new Cat(name, parseDate, gender.name(), breedId, sterilized, invalid,
                        kindOfAnimal.name(), shelter);
                logger.info("метод savePetToDB - кошка сохранена в БД: " + name + ", id: " + cat.getId());
                catRepository.save(cat);
                return true;
            }
        }
        logger.info("метод savePetToDB - выбран приют с неверной специализацией "
                + specialization+" вместо "+ kindOfAnimal.name());
        return false;
    }

}
