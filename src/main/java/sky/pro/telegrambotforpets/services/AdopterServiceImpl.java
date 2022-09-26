package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.AdopterService;
import sky.pro.telegrambotforpets.model.*;
import sky.pro.telegrambotforpets.repositories.CatAdopterRepository;
import sky.pro.telegrambotforpets.repositories.DogAdopterRepository;
import sky.pro.telegrambotforpets.repositories.GuestRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class AdopterServiceImpl implements AdopterService {

    private final Logger logger = LoggerFactory.getLogger(AdopterServiceImpl.class);
    private final DogAdopterRepository dogAdoptRep;
    private final CatAdopterRepository catAdoptRep;
    private final GuestRepository guestRepository;

    public AdopterServiceImpl(DogAdopterRepository dogAdoptRep, CatAdopterRepository catAdoptRep, GuestRepository guestRepository) {
        this.dogAdoptRep = dogAdoptRep;
        this.catAdoptRep = catAdoptRep;
        this.guestRepository = guestRepository;
    }


    @Override
    public boolean saveAdopterToDB(String name, String middleName, String lastName, Gender gender,
                                   String birthday, String phoneNumber, String address, KindOfAnimal kindOfAnimal) {
        LocalDate parseDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        switch (kindOfAnimal) {
            case DOGS -> {
                DogAdopter dogAdopter = new DogAdopter(name, middleName, lastName, gender.name(), parseDate,
                        phoneNumber, address, kindOfAnimal.name());
                if (!adopterAlreadyExists(dogAdopter)) {
                    dogAdoptRep.save(dogAdopter);
                    Long chatId = getChatIdIfExists(dogAdopter);//если метод вернул chatId - устанавливаю его этому усыновителю
                    if (chatId != null) {
                        logger.info("найден chatId гостя по номеру телефона и установлен данному усыновителю");
                        dogAdopter.setChatId(chatId);
                    }
                    logger.info("метод saveAdopterToDB - сохранен усыновитель " + dogAdopter.getName());
                    return true;
                } else {
                    logger.info("метод saveAdopterToDB - такой усыновитель уже есть в БД");
                    return false;
                }
            }
            case CATS -> {
                CatAdopter catAdopter = new CatAdopter(name, middleName, lastName, gender.name(), parseDate,
                        phoneNumber, address, kindOfAnimal.name());
                if (!adopterAlreadyExists(catAdopter)) {
                    catAdoptRep.save(catAdopter);
                    Long chatId = getChatIdIfExists(catAdopter);//если метод вернул chatId - устанавливаю его этому усыновителю
                    if (chatId != null) {
                        logger.info("найден chatId гостя по номеру телефона и установлен данному усыновителю");
                        catAdopter.setChatId(chatId);
                    }
                    logger.info("метод saveAdopterToDB - сохранен усыновитель " + catAdopter.getName());
                    return true;
                } else {
                    logger.info("метод saveAdopterToDB - такой усыновитель уже есть в БД");
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * проверяет в БД гостей, есть ли гость с указанным номером телефона и если есть, то возвращает его chatId
     *
     * @param adopter
     * @return
     */
    private Long getChatIdIfExists(Adopter adopter) {
        String phoneNumber = adopter.getPhoneNumber();
        Optional<Guest> guestByPhoneNumber = guestRepository.findByPhoneNumber(phoneNumber);
        return guestByPhoneNumber.map(Guest::getChatId).orElse(null);
    }

    @Override
    public boolean editAdopter(Long adopterId, KindOfAnimal kindOfAnimal, String name, String middleName,
                               String lastName, Gender gender, String birthday, String phoneNumber,
                               String address) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    DogAdopter dogAdopter = dogAdoptRep.findById(adopterId).get();
                    checkAndFillFields(dogAdopter, name, middleName, lastName, gender, birthday, phoneNumber, address);
                    if (dogAdopter.getChatId() == null) {//добавил проверку, чтобы достать chatId по номеру телефона
                        Long chatId = getChatIdIfExists(dogAdopter);
                        if (chatId != null) {
                            dogAdopter.setChatId(chatId);
                        }
                    }
                    logger.info("метод editAdopter - поля обновлены");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    logger.info("метод editAdopter - такого усыновитея нет в БД");
                    return false;
                }
            }
            case CATS -> {
                try {
                    CatAdopter catAdopter = catAdoptRep.findById(adopterId).get();
                    checkAndFillFields(catAdopter, name, middleName, lastName, gender, birthday, phoneNumber, address);
                    if (catAdopter.getChatId() == null) {//добавил проверку, чтобы достать chatId по номеру телефона
                        Long chatId = getChatIdIfExists(catAdopter);
                        if (chatId != null) {
                            catAdopter.setChatId(chatId);
                        }
                    }
                    logger.info("метод editAdopter - поля обновлены");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    logger.info("метод editAdopter - такого усыновитея нет в БД");
                    return false;
                }
            }
        }
        return false;
    }//по номеру тел проверить есть ли chatId???

    private void checkAndFillFields(Adopter adopter, String name, String middleName,
                                    String lastName, Gender gender, String birthday, String phoneNumber,
                                    String address) {
        if (name != null) {
            adopter.setName(name);
        }
        if (middleName != null) {
            adopter.setMiddleName(middleName);
        }
        if (lastName != null) {
            adopter.setLastName(lastName);
        }
        if (gender != null) {
            adopter.setGender(gender.name());
        }
        if (birthday != null) {
            LocalDate parseDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            adopter.setBirthday(parseDate);
        }
        if (phoneNumber != null) {
            adopter.setPhoneNumber(phoneNumber);
        }
        if (address != null) {
            adopter.setAddress(address);
        }
    }


    /**
     * ищет усыновителя по Id и виду животного
     *
     * @param id
     * @param kindOfAnimal
     * @return Adopter
     */
    @Override
    public Adopter getAdopterById(Long id, KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    DogAdopter dogAdopter = dogAdoptRep.findById(id).get();
                    logger.info("метод getAdopterById - вернул усыновителя с именем " + dogAdopter.getName());
                    return dogAdopter;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    return null;
                }
            }
            case CATS -> {
                try {
                    CatAdopter catAdopter = catAdoptRep.findById(id).get();
                    logger.info("метод getAdopterById - вернул усыновителя с именем " + catAdopter.getName());
                    return catAdopter;
                } catch (NoSuchElementException e) {
                    logger.info(e.toString());
                    return null;
                }
            }
        }
        logger.info("Усыновитель с таким Id в БД не найден");
        return null;
    }

    @Override
    public Adopter getAdopterByPhoneNumber(KindOfAnimal kindOfAnimal, String phoneNumber) {
        switch (kindOfAnimal) {
            case DOGS -> dogAdoptRep.findByPhoneNumber(phoneNumber).orElse(null);

            case CATS -> catAdoptRep.findByPhoneNumber(phoneNumber).orElse(null);
        }
        return null;
    }

    @Override
    public Collection<? extends Adopter> getListAdopters(KindOfAnimal kindOfAnimal) {
        switch (kindOfAnimal) {
            case DOGS -> {
                return dogAdoptRep.findAll();
            }
            case CATS -> {
                return catAdoptRep.findAll();
            }
        }
        return null;
    }

    @Override
    public boolean removeAdopter(KindOfAnimal kindOfAnimal, Long adopterId) {
        switch (kindOfAnimal) {
            case DOGS -> {
                try {
                    DogAdopter dogAdopter = dogAdoptRep.findById(adopterId).get();//сначала получаю усыновителя по id,
                    // а потом удалаю, т.к. выскакивают UnexpectedRollbackException и EmptyResultDataAccessException,
                    // при том что обрабатываю их в блоке catch, что-то с Transactional, но пока не разобрался
                    dogAdoptRep.delete(dogAdopter);
                    logger.info("метод removeAdopter - усыновитель с id: " + adopterId + " удален");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info("метод removeAdopter - усыновитель с id: " + adopterId + " не найден");
                    logger.info(e.toString());
                    return false;
                }
            }
            case CATS -> {
                try {
                    CatAdopter catAdopter = catAdoptRep.findById(adopterId).get();//сначала получаю усыновителя по id,
                    // а потом удалаю, т.к. выскакивают UnexpectedRollbackException и EmptyResultDataAccessException,
                    // при том что обрабатываю их в блоке catch, что-то с Transactional, но пока не разобрался
                    catAdoptRep.delete(catAdopter);
                    logger.info("метод removeAdopter - усыновитель с id: " + adopterId + " удален");
                    return true;
                } catch (NoSuchElementException e) {
                    logger.info("метод removeAdopter - усыновитель с id: " + adopterId + " не найден");
                    logger.info(e.toString());
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * проверяет не существет ли уже такой усыновитель в БД
     *
     * @param adopter
     * @return
     */
    private boolean adopterAlreadyExists(Adopter adopter) {
        boolean dogAdopterAlreadyExists = dogAdoptRep.findAll().contains(adopter);
        boolean catAdopterAlreadyExists = catAdoptRep.findAll().contains(adopter);

        if (dogAdopterAlreadyExists || catAdopterAlreadyExists) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Adopter> getAdopterByChatIdAndKindOfAnimal(Long chatId, KindOfAnimal kindOfAnimal) {
        Optional<Adopter> adopter = null;
        logger.info("метод getAdopterByChatIdAndKindOfAnimal");
        switch (kindOfAnimal) {
            case CATS -> {
                return catAdoptRep.getCatAdopterByChatId(chatId);
            }
            case DOGS -> {
                return dogAdoptRep.getDogAdopterByChatId(chatId);
            }
        }
        logger.info("метод getAdopterByChatIdAndKindOfAnimal exit");
        return null;
    }

    /**
     * ищет по chatId в БД усыновителей и достает имя, если усыновителя с таким chatId нет, то идет в БД
     * гостей и достает гостя с таким chatId и возвращает имя
     *
     * @param chatId
     * @return
     */
    @Override
    public String greeting(Long chatId) {
        Optional<DogAdopter> dogAdopter = dogAdoptRep.findByChatId(chatId);
        Optional<CatAdopter> catAdopter = catAdoptRep.findByChatId(chatId);
        if (dogAdopter.isPresent()) {
            logger.info("метод greeting " + dogAdopter.get().getName());
            return dogAdopter.get().getName();
        } else if (catAdopter.isPresent()) {
            return catAdopter.get().getName();
        } else {
            Optional<Guest> guest = guestRepository.findByChatId(chatId);
            if (guest.isPresent()) {
                return guest.get().getUserName();
            } else {
                return "Незнакомец";
            }
        }
    }
}
