package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.model.DogHandler;

import java.util.List;

public interface DogHandlerService {

    boolean saveDogHandlerToDB(DogHandler dogHandler);
    boolean editDogHandler(DogHandler dogHandler);

    DogHandler getDogHandler(Long id);

    List<DogHandler> getAllDogHandlers();

    boolean removeDogHandler(Long id);
}
