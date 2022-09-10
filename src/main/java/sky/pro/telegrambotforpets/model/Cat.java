package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Cat extends Pet{

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    @OneToOne
    private CatAdopter catAdopter;

    public Cat() {
    }

    public Cat(String name, LocalDate birthday, String gender, Long breedId, Boolean sterilized, Boolean invalid, String kindOfAnimal, Shelter shelter) {
        super(name, birthday, gender, breedId, sterilized, invalid, kindOfAnimal);
        this.shelter = shelter;
    }

    public CatAdopter getCatAdopter() {
        return catAdopter;
    }

    public void setCatAdopter(CatAdopter catAdopter) {
        this.catAdopter = catAdopter;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }
}
