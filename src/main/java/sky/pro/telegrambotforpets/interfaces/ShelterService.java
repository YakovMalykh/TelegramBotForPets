package sky.pro.telegrambotforpets.interfaces;

import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.model.Shelter;

import java.io.IOException;
import java.util.List;

public interface ShelterService {

    boolean saveShelterToDB(Shelter shelter, MultipartFile howToGet, MultipartFile safetyRules) throws IOException;

    boolean editShelter(Shelter shelter);

    Shelter getShelter(Long id);

    List<Shelter> getAllShelters();

    boolean removeShelter(Long id);
}
