package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.constants.AdoptionsResult;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.model.Adoption;

import java.util.List;
import java.util.Optional;

public interface AdoptionService {

    /**
     * сохраняет новую запись об усыновлении в БД
     *
     * @param kindOfAnimal
     * @param petId
     * @param adopterId
     * @return
     * @see AdoptionService#saveAdoptionToDB
     */
    boolean saveAdoptionToDB(KindOfAnimal kindOfAnimal, Long petId, Long adopterId);

    /**
     * изменяет поле результат усыновления, по этому полю будем искать записи, по которым срок адаптации увеличен
     * и отправляет в чат усыновителю результат испытательного периода
     * @param adoptionId
     * @param adoptionsResult
     * @return
     */
    boolean setAdoptionsResult (Long adoptionId, AdoptionsResult adoptionsResult);

    Optional<Adoption> getAdoptionById(Long adoptionId);

    Optional<Adoption> getFirstAdoptionByAdopterIdAndKindOfAnimal(Long adoptionId,String kindOfAnimal);

    List<Optional<Adoption>> getAdoptionByAdopterId(Long adopterId);

    // Optional<Adoption> getAdobtionByAdopterId (Long adopterId);
    List<Adoption> getAllAdoptions();

    /**
     * выбирает записи у которых испытательный срок заканчивается сегодня
     * @return
     */
    List<Adoption> trialPeriodEndsToday();

    /**
     * удаляет запись об усыновлении из БД и обнуляет поле Усыновитель у питомца
     * @param adoptionId
     * @return
     */
    boolean removeAdoption(Long adoptionId);
}
