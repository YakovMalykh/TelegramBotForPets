package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.AdopterService;
import sky.pro.telegrambotforpets.interfaces.PetService;
import sky.pro.telegrambotforpets.interfaces.ShelterService;
import sky.pro.telegrambotforpets.model.*;
import sky.pro.telegrambotforpets.repositories.CatRepository;
import sky.pro.telegrambotforpets.repositories.DogRepository;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static sky.pro.telegrambotforpets.constants.KindOfAnimal.*;

@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final ShelterService shelterService;
    private final AdopterService adopterService;

    public PetServiceImpl(DogRepository dogRepository, CatRepository catRepository, ShelterService shelterService, AdopterService adopterService) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.shelterService = shelterService;
        this.adopterService = adopterService;
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
        if (shelter != null) {
            String specialization = shelter.getSpecialization();

            LocalDate parseDate = LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            switch (kindOfAnimal) {
                case DOGS:
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
                    break;
                case CATS:
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
                    break;
            }
            logger.info("метод savePetToDB - выбран приют с неверной специализацией "
                    + specialization + " вместо " + kindOfAnimal.name());
            return false;
        } else {
            logger.info("приюта с таким Id нет в БД");
            return false;
        }
    }

    @Override
    public Pet getPetById(Long petId, KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    Dog dog = dogRepository.findById(petId).get();
                    logger.info("метод getPetById - найдена собака - " + dog.getName());
                    return dog;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    logger.info("собака с таким id не найдена");
                    return null;
                }
            }
            case CATS -> {
                try {
                    Cat cat = catRepository.findById(petId).get();
                    logger.info("метод getPetByName - найдена кошка - " + cat.getName());
                    return cat;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    logger.info("кошка с таким id не найдена");
                    return null;
                }
            }
        }
        logger.info("питомец с таким id в БД не найден");
        return null;
    }

    /**
     * ищет питомца по имени в соответствующей виду животного таблице
     *
     * @param name
     * @param kindOfAnimal
     * @return Pet и наследников
     * @see DogRepository#findByNameIgnoreCase
     * @see CatRepository#findByNameIgnoreCase
     */

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

    /**
     * получение всего списка питомцев данного вида
     *
     * @param kindOfAnimal
     * @return Collection Dog or Cat
     * @see CatRepository#findAll()
     * @see DogRepository#findAll()
     */
    @Override
    public Collection<? extends Pet> getListOfAllPets(KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                List<Dog> dogs = dogRepository.findAll();
                logger.info("метод getListOfAllPets - проверяется БД собак");
                return dogs;
            }
            case CATS -> {
                List<Cat> cats = catRepository.findAll();
                logger.info("метод getListOfAllPets - проверяется БД кошек");
                return cats;
            }
        }
        return null;
    }

    @Override
    public boolean editPet(Long petId, KindOfAnimal kindOfAnimal,
                           String name, String birthDay, Gender gender, Long breedId,
                           Boolean sterilized, Boolean invalid, Long shelterId) {

        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    Dog dog = dogRepository.findById(petId).get();
                    checkAndFillFields(dog, name, birthDay, gender, breedId, sterilized, invalid, shelterId);
                    logger.info("метод editPet - поля обновлены");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    logger.info("метод editPet - питомца с таким ID нет ы БД");
                    return false;
                }
            }
            case CATS -> {
                try {
                    Cat cat = catRepository.findById(petId).get();
                    checkAndFillFields(cat, name, birthDay, gender, breedId, sterilized, invalid, shelterId);
                    logger.info("метод editPet - поля обновлены");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    logger.info("метод editPet - питомца с таким ID нет ы БД");
                    return false;
                }
            }
        }

        return false;
    }


    /**
     * проверяет на пустоту переданные параметры и если непустые обновляет поля в питомце
     *
     * @param pet
     * @param name
     * @param birthDay
     * @param gender
     * @param breedId
     * @param sterilized
     * @param invalid
     * @param shelterId
     */
    private void checkAndFillFields(Pet pet, String name, String birthDay, Gender gender, Long breedId,
                                    Boolean sterilized, Boolean invalid, Long shelterId) {
        switch (valueOf(pet.getKindOfAnimal())) {
            case DOGS -> {
                Dog dog = dogRepository.findById(pet.getId()).get();
                if (name != null) {
                    dog.setName(name);
                }
                if (birthDay != null) {
                    LocalDate parseDate = LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    dog.setBirthday(parseDate);
                }
                if (gender != null) {
                    dog.setGender(gender.name());
                }
                if (breedId != null) {
                    dog.setBreedId(breedId);
                }
                if (sterilized != null) {
                    dog.setSterilized(sterilized);
                }
                if (invalid != null) {
                    dog.setInvalid(invalid);
                }
                if (shelterId != null) {
                    dog.setShelter(shelterService.getShelter(shelterId));
                }
            }
            case CATS -> {
                Cat cat = catRepository.findById(pet.getId()).get();
                if (!(name == null && name.isEmpty() && name.isBlank())) {
                    cat.setName(name);
                }
                if (!(birthDay == null && name.isEmpty() && name.isBlank())) {
                    LocalDate parseDate = LocalDate.parse(birthDay, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    cat.setBirthday(parseDate);
                }
                if (gender != null) {
                    cat.setGender(gender.name());
                }
                if (breedId != null) {
                    cat.setBreedId(breedId);
                }
                if (sterilized != null) {
                    cat.setSterilized(sterilized);
                }
                if (invalid != null) {
                    cat.setInvalid(invalid);
                }
                if (shelterId != null) {
                    cat.setShelter(shelterService.getShelter(shelterId));
                }
            }
        }
    }

    /**
     * вносит усыновителя живтоного в БД.
     *
     * @param kindOfAnimal
     * @param petId
     * @param adopterId
     * @return
     * @see AdopterService#getAdopterById
     */
    @Override
    public boolean appointAdopter(KindOfAnimal kindOfAnimal, Long petId, Long adopterId) {
        Adopter adopter = adopterService.getAdopterById(adopterId, kindOfAnimal);
        Pet pet = getPetById(petId, kindOfAnimal);

        if (adopter != null && pet != null) {
            switch (kindOfAnimal) {
                case DOGS -> {
                    Dog dog = (Dog) pet;
                    dog.setDogAdopter((DogAdopter) adopter);
                    ((DogAdopter) adopter).setDog(dog);
                    logger.info("метод appointAdopter - собаке " + dog.getName() + " установлен усыновитель "
                            + adopter.getName());
                    return true;
                }
                case CATS -> {
                    Cat cat = (Cat) pet;
                    cat.setCatAdopter((CatAdopter) adopter);
                    ((CatAdopter)adopter).setCat(cat);
                    logger.info("метод appointAdopter - собаке " + cat.getName() + " установлен усыновитель "
                            + adopter.getName());
                    return true;
                }
            }
        }
        logger.info("метод appointAdopter - усыновителя или питомца с такими Id не существует в БД");
        return false;
    }

    /**
     * удаляет питомца по ID из соответсвующей виду животного таблицы
     *
     * @param kindOfAnimal
     * @param petId
     * @return
     * @see DogRepository#deleteById
     * @see CatRepository#deleteById
     */
    @Override
    public boolean removePet(KindOfAnimal kindOfAnimal, Long petId) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    Dog dog = dogRepository.findById(petId).get();//сначала получаю питомца по id,
                    // а потом удалаю, т.к. выскакивают UnexpectedRollbackException и EmptyResultDataAccessException,
                    // при том что обрабатывал их в блоке catch, что-то с Transactional, но пока не разобрался
                    dogRepository.delete(dog);
                    logger.info("метод removePet - собака с id: " + petId + " удалена");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info("метод removePet - собаки с id: " + petId + " в БД нет");
                    System.out.println(e);
                    return false;
                }
            }
            case CATS -> {
                try {
                    Cat cat = catRepository.findById(petId).get();//сначала получаю питомца по id,
                    // а потом удалаю, т.к. выскакивают UnexpectedRollbackException и EmptyResultDataAccessException,
                    // при том что обрабатывал их в блоке catch, что-то с Transactional, но пока не разобрался
                    catRepository.delete(cat);
                    logger.info("метод removePet - кошка с id: " + petId + " удалена");
                    return true;
                } catch (NoSuchElementException e) {
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
