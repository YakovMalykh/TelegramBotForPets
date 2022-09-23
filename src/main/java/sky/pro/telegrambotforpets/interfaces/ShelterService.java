package sky.pro.telegrambotforpets.interfaces;

import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.model.Shelter;

import java.io.IOException;
import java.util.List;

public interface ShelterService {

    boolean saveShelterToDB(Shelter shelter, MultipartFile howToGet, MultipartFile safetyRules) throws IOException;

    boolean editShelter(Long id, String name, String address, String schedule, KindOfAnimal kindOfAnimal,
                        String description, String securityPhoneNumber, MultipartFile howToGet,
                        MultipartFile safetyRules) throws IOException;

    /**
     * возвращает приют по ID
     * @param id
     * @return Shelter
     */
    Shelter getShelter(Long id);

    List<Shelter> getAllShelters();

    boolean removeShelter(Long id);

    Shelter findShelterBySpecialization(String name);
}