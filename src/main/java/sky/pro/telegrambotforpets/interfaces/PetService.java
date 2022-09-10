package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.model.Pet;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface PetService {

    boolean savePetToDB(String name, String birthDay, Gender gender, Long breedId, Boolean sterilized,
                        Boolean invalid, KindOfAnimal kindOfAnimal, Long shelterId);

    Pet getPetById(Long petId, KindOfAnimal kindOfAnimal);

    Pet getPetByName(String name, KindOfAnimal kindOfAnimal);

    Collection<? extends Pet> getListOfAllPets(KindOfAnimal kindOfAnimal);

    boolean editPet(Long petId, KindOfAnimal kindOfAnimal, String name, String birthDay, Gender gender,
                    Long breedId, Boolean sterilized, Boolean invalid, Long shelterId);
    boolean appointAdopter(KindOfAnimal kindOfAnimal, Long petId, Long adopterId);
    boolean removePet(KindOfAnimal kindOfAnimal, Long petId);

}
