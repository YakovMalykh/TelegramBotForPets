package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.model.Pet;

import javax.xml.crypto.Data;

public interface PetService {

    public boolean savePetToDB(String name, String birthDay, Gender gender, Long breedId, Boolean sterilized,
                               Boolean invalid, KindOfAnimal kindOfAnimal, Long shelterId);
}
