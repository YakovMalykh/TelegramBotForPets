package sky.pro.telegrambotforpets.model;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
public class Adopter extends Person {


    private String kindOfAnimal;

    public Adopter() {
    }

    public Adopter(String name, String middleName, String lastName, String gender, LocalDate birthday,
                   String phoneNumber, String address, String kindOfAnimal) {
        super(name, middleName, lastName, gender, birthday, phoneNumber, address);
        this.kindOfAnimal = kindOfAnimal;
    }

    public String getKindOfAnimal() {
        return kindOfAnimal;
    }

    /**
     * соответствует Enum KindOfAnimal
     */
    public void setKindOfAnimal(String kindOfAnimal) {
        this.kindOfAnimal = kindOfAnimal;
    }
}
