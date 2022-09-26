package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class DogAdopter extends Adopter{

    private Long chatId;
    @OneToOne
    private Dog dog;

    public DogAdopter() {
    }

    public DogAdopter(String name, String middleName, String lastName, String gender, LocalDate birthday,
                      String phoneNumber, String address, String kindOfAnimal) {
        super(name, middleName, lastName, gender, birthday, phoneNumber, address, kindOfAnimal);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }


}
