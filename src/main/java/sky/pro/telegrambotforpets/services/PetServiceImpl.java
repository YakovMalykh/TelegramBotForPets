package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingPathVariableException;
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.PetService;
import sky.pro.telegrambotforpets.interfaces.ShelterService;
import sky.pro.telegrambotforpets.model.Cat;
import sky.pro.telegrambotforpets.model.Dog;
import sky.pro.telegrambotforpets.model.Pet;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.CatRepository;
import sky.pro.telegrambotforpets.repositories.DogRepository;

import javax.xml.crypto.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    /**
     * сохраняет нового питомца в сообтетсвующую БД
     *
     * @param name
     * @param birthDay
     * @param gender
     * @param breedId
     * @param sterilized
     * @param invalid
     * @param kindOfAnimal
     * @param shelterId
     * @return boolean
     */
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
                if (!petAlreadyExists(dog)) {
                    dogRepository.save(dog);
                    logger.info("метод savePetToDB - собака сохранена в БД: " + name);
                    return true;
                } else {
                    logger.info("метод savePetToDB - такая собака уже есть в БД");
                    return false;
                }
            }
        } else if (kindOfAnimal == CATS) {
            if (specialization.equals(kindOfAnimal.name())) {
                Cat cat = new Cat(name, parseDate, gender.name(), breedId, sterilized, invalid,
                        kindOfAnimal.name(), shelter);
                if (!petAlreadyExists(cat)) {
                    logger.info("метод savePetToDB - кошка сохранена в БД: " + name + ", id: " + cat.getId());
                    catRepository.save(cat);
                    return true;
                } else {
                    logger.info("метод savePetToDB - такая кошка уже есть в БД");
                    return false;
                }
            }
        }
        logger.info("метод savePetToDB - выбран приют с неверной специализацией "
                + specialization + " вместо " + kindOfAnimal.name());
        return false;
    }

    @Override
    public Pet getPetById(Long petId, KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    Dog dog = dogRepository.findById(petId).get();
                    logger.info("метод getPetByName - найдена собака - " + dog.getName());
                    return dog;
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                    return null;
                }
            }
            case CATS -> {
                try {
                    Cat cat = catRepository.findById(petId).get();
                    logger.info("метод getPetByName - найдена кошка - " + cat.getName());
                    return cat;
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                    return null;
                }
            }
        }
        logger.info("питомец с таким id в БД не найден");
        return null;
    }

    @Override
    public Pet getPetByName(String name, KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    Dog dog = dogRepository.findByNameIgnoreCase(name);
                    logger.info("метод getPetByName - найдена собака - " + dog.getName());
                    return dog;
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                    return null;
                }
            }
            case CATS -> {
                try {
                    Cat cat = catRepository.findByNameIgnoreCase(name);
                    logger.info("метод getPetByName - найдена кошка - " + cat.getName());
                    return cat;
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                    return null;
                }
            }
        }
        logger.info("питомец с таким именем в БД не найден");
        return null;
    }

    @Override
    public Collection<? extends Pet> getListOfAllPets(KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                // привожу к родительскому класса
                List<Dog> dogs = dogRepository.findAll();
                logger.info("метод getListOfAllPets - проверяется БД собак");
                return dogs;
            }
            case CATS -> {
                // привожу к родительскому класса
                List<Cat> cats = catRepository.findAll();
                logger.info("метод getListOfAllPets - проверяется БД кошек");
                return cats;
            }
        }
        return null;
    }

    @Override
    public boolean editPet(Long petId, KindOfAnimal kindOfAnimal, String name, String birthDay, Gender gender, Long breedId, Boolean sterilized, Boolean invalid, Long shelterId) {
        return false;
    }

    @Override
    public boolean appointAdopter(KindOfAnimal kindOfAnimal, Long petId, Long adopterId) {
        // нужно инжектить сервис усыновителей, и использовать его метод getAdopterById
        return false;
    }

    @Override
    public boolean removePet(KindOfAnimal kindOfAnimal, Long petId) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    dogRepository.deleteById(petId);
                    logger.info("метод removePet - собака с id: " + petId + " удалена");
                    return true;
                } catch (EmptyResultDataAccessException e) {
                    logger.info("метод removePet - собаки с id: " + petId + " в БД нет");
                    System.out.println(e);
                    return false;
                }
            }
            case CATS -> {
                try {
                    catRepository.deleteById(petId);
                    logger.info("метод removePet - кошка с id: " + petId + " удалена");
                    return true;
                } catch (EmptyResultDataAccessException e) {
                    logger.info("метод removePet - кошки с id: " + petId + " в БД нет");
                    System.out.println(e);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * проверяет существует ли такой питомец в БД
     *
     * @param pet
     * @return boolean
     */
    private boolean petAlreadyExists(Pet pet) {
        boolean dogAlreadyExists = dogRepository.findAll().contains(pet);
        boolean catAlreadyExists = catRepository.findAll().contains(pet);

        if (dogAlreadyExists || catAlreadyExists) {
            return true;
        } else {
            return false;
        }
    }


}
