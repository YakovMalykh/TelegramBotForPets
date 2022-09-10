package sky.pro.telegrambotforpets.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Adopter extends Person{


    private String kindOfAnimal;

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
