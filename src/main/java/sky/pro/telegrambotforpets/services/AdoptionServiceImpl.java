package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.AdoptionsResult;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.AdopterService;
import sky.pro.telegrambotforpets.interfaces.AdoptionService;
import sky.pro.telegrambotforpets.interfaces.CheckService;
import sky.pro.telegrambotforpets.interfaces.PetService;
import sky.pro.telegrambotforpets.model.Adoption;
import sky.pro.telegrambotforpets.model.Cat;
import sky.pro.telegrambotforpets.model.Dog;
import sky.pro.telegrambotforpets.repositories.AdoptionRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static sky.pro.telegrambotforpets.constants.AdoptionsResult.*;

@Service
@Transactional
public class AdoptionServiceImpl implements AdoptionService {

    Logger logger = LoggerFactory.getLogger(AdoptionServiceImpl.class);

    private final AdoptionRepository adoptionRepository;
    private final PetService petService;
    private final AdopterService adopterService;
    private final CheckService checkService;

    public AdoptionServiceImpl(AdoptionRepository adoptionRepository, PetService petService, AdopterService adopterService, CheckService checkService) {
        this.adoptionRepository = adoptionRepository;
        this.petService = petService;
        this.adopterService = adopterService;
        this.checkService = checkService;
    }

    /**
     * сохраняет новую запись об усыновлении в БД
     *
     * @param kindOfAnimal
     * @param petId
     * @param adopterId
     * @return
     * @see AdoptionService#saveAdoptionToDB
     */
    @Override
    public boolean saveAdoptionToDB(KindOfAnimal kindOfAnimal, Long petId, Long adopterId) {
        boolean adopterAppointed = petService.appointAdopter(kindOfAnimal, petId, adopterId);
        LocalDate today = LocalDate.now();
        if (adopterAppointed) {
            Adoption adoption = new Adoption(kindOfAnimal.name(), petId, adopterId, today);
            adoptionRepository.save(adoption);
            logger.info("метод saveAdoptionToDB - запись об усыновлении создана в БД");
            return true;
        }
        logger.info("метод saveAdoptionToDB - запись об усыновлении не создана, т.к. в методе appointAdopter " +
                "что-то пошло не так");
        return false;
    }

    /**
     * изменяет поле результат усыновления, по этому полю будем искать записи, по которым срок адаптации увеличен
     * и отправляет в чат усыновителю результат испытательного периода
     *
     * @param adoptionId
     * @param adoptionsResult
     * @return
     */
    @Override
    public boolean setAdoptionsResult(Long adoptionId, AdoptionsResult adoptionsResult) {
        Optional<Adoption> adoption = adoptionRepository.findById(adoptionId);
        if (adoption.isPresent()) {
            adoption.get().setAdoptionsResult(adoptionsResult.name());
            checkService.notifications(adoption.get(), adoptionsResult);
            logger.info("метод setAdoptionsResult - поле результат усыновления успешно изменено");
            return true;
        } else {
            logger.info("метод setAdoptionsResult - записи с таким ID не найдено");
            return false;
        }
    }

    @Override
    public Optional<Adoption> getAdoptionById(Long adoptionId) {
        return adoptionRepository.findById(adoptionId);
    }
    @Override
    public Optional<Adoption> getFirstAdoptionByAdopterIdAndKindOfAnimal(Long adoptionId, String kindOfAnimal) {
        return adoptionRepository.findFirstByAdopterIdAndKindOfAnimal(adoptionId, kindOfAnimal);
    }
    @Override
    public List<Optional<Adoption>> getAdoptionByAdopterId(Long adopterId) {return adoptionRepository.findAdoptionByAdopterId(adopterId);}
    @Override
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    // @Override
    // public  Adoption adoption AdoptionServiceImpl(Long adopterId) {
    // }

    /**
     * выбирает записи у которых испытательный срок заканчивается сегодня
     *
     * @return
     */
    @Override
    public List<Adoption> trialPeriodEndsToday() {
        LocalDate today = LocalDate.now();
        LocalDate period30daysStarted = today.minusDays(30);
        LocalDate period44daysStarted = today.minusDays(44);
        LocalDate period60daysStarted = today.minusDays(60);
        List<Adoption> adoptionsByDate = adoptionRepository.findByAdoptionsDateOrAdoptionsDateOrAdoptionsDate(
                period30daysStarted, period44daysStarted, period60daysStarted);
        List<Adoption> adoptions = adoptionsByDate.stream().filter(e ->
                        valueOf(e.getAdoptionsResult()) != SUCCESS && valueOf(e.getAdoptionsResult()) != FAIL)
                .toList();
        return adoptions;
    }

    /**
     * удаляет запись об усыновлении из БД и обнуляет поле Усыновитель у питомца
     *
     * @param adoptionId
     * @return
     */
    @Override// из контроллера удалил этот метод, т.к. усыновление должно закончиться логически либо успехом,
    // либо провалом
    public boolean removeAdoption(Long adoptionId) {
        Optional<Adoption> adoption = adoptionRepository.findById(adoptionId);
        if (adoption.isPresent()) {
            KindOfAnimal kindOfAnimal = KindOfAnimal.valueOf(adoption.get().getKindOfAnimal());
            Long petId = adoption.get().getPetId();
            // обнуляем поле Усыновитель
            switch (kindOfAnimal) {
                case DOGS -> {
                    Dog dog = (Dog) petService.getPetById(petId, kindOfAnimal);
                    dog.setDogAdopter(null);
                }
                case CATS -> {
                    Cat cat = (Cat) petService.getPetById(petId, kindOfAnimal);
                    cat.setCatAdopter(null);
                }
            }
            // удаляем запись
            adoptionRepository.deleteById(adoptionId);
            logger.info("метод removeAdoption - записи об усыновлении удалена, поле Усыновитель у питомца обнолено");
            return true;
        } else {
            logger.info("метод removeAdoption - записи об усыновлении с таким ID в БД не найдено");
            return false;
        }
    }
}
