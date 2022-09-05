package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.interfaces.DogHandlerService;
import sky.pro.telegrambotforpets.model.DogHandler;
import sky.pro.telegrambotforpets.repositories.DogHandlerRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DogHandlerServiceImpl implements DogHandlerService {
    private Logger logger = LoggerFactory.getLogger(DogHandlerServiceImpl.class);

    private final DogHandlerRepository dogHandlerRepository;

    public DogHandlerServiceImpl(DogHandlerRepository dogHandlerRepository) {
        this.dogHandlerRepository = dogHandlerRepository;
    }

    @Override
    public boolean saveDogHandlerToDB(DogHandler dogHandler) {
        if (!dogHandlerRepository.findAll().contains(dogHandler)) {
            dogHandlerRepository.save(dogHandler);
            logger.info("метод saveDogHandlerToDB - сохранен новый кинолог");
            return true;
        } else {
            logger.info("метод saveDogHandlerToDB - такой кинолог уже есть в БД");
            return false;
        }
    }

    /**
     * редкатирует существующего в БД кинолога
     * @param dogHandler
     * @return
     */
    @Override
    public boolean editDogHandler(DogHandler dogHandler) {
        if (!dogHandlerRepository.findAll().contains(dogHandler)) {
            logger.info("метод editDogHandler - кинолог не найден");
            return false;
        } else {
            dogHandlerRepository.save(dogHandler);
            logger.info("метод editDogHandler - кинолог " + dogHandler.getId() + " изменен");
            return true;
        }
    }

    /**
     * возвращает кинолога по id
     * @param id
     * @return DogHandler
     */
    @Override
    public DogHandler getDogHandler(Long id) {
        return dogHandlerRepository.findById(id).get();
    }

    /**
     * возвращает список из всех кинологов
     * @return
     */
    @Override
    public List<DogHandler> getAllDogHandlers() {
        return dogHandlerRepository.findAll();
    }

    @Override
    public boolean removeDogHandler(Long id) {
        if (getDogHandler(id) != null) {
            dogHandlerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
