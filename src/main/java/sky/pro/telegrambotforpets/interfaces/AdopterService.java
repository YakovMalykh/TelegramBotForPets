package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.model.Adopter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface AdopterService {
    boolean saveAdopterToDB(String name, String middleName, String lastName, Gender gender, String birthday,
                            String phoneNumber, String address, KindOfAnimal kindOfAnimal);//по номеру тел проверить есть ли chatId

    boolean editAdopter(Long adopterId, KindOfAnimal kindOfAnimal, String name, String middleName,
                        String lastName, Gender gender, String birthday, String phoneNumber, String address);//по номеру тел проверить есть ли chatId

    Adopter getAdopterById(Long id, KindOfAnimal kindOfAnimal);

    Adopter getAdopterByPhoneNumber(KindOfAnimal kindOfAnimal, String phoneNumber);

    Collection<? extends Adopter> getListAdopters(KindOfAnimal kindOfAnimal);

    boolean removeAdopter(KindOfAnimal kindOfAnimal, Long adopterId);
}
