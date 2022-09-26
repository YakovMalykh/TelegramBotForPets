package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class CatAdopter extends Adopter{
    private Long chatId;

    @OneToOne
    private Cat cat;

    public CatAdopter() {
    }

    public CatAdopter(String name, String middleName, String lastName, String gender, LocalDate birthday,
                      String phoneNumber, String address, String kindOfAnimal) {
        super(name, middleName, lastName, gender, birthday, phoneNumber, address, kindOfAnimal);
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

}
