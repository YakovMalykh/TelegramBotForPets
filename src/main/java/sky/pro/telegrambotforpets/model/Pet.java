package sky.pro.telegrambotforpets.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * абстрактный класс, чтобы наследовать от него клсс Dog и Cat
 */
@MappedSuperclass
public abstract class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthday;
    /**
     * проверяется на соответствеи Enum Gender
     */
    private String gender;
    private Long breedId;// позже можно реализовать отдельный класс для пород
    private Boolean sterilized;
    private Boolean invalid;

    /**
     * соответствует Enum KindOfAnimal
     */
    private String kindOfAnimal;




    public Pet() {
    }

    public Pet(String name, LocalDate birthday, String gender, Long breedId, Boolean sterilized, Boolean invalid, String kindOfAnimal) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.breedId = breedId;
        this.sterilized = sterilized;
        this.invalid = invalid;
        this.kindOfAnimal = kindOfAnimal;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getBreedId() {
        return breedId;
    }

    public void setBreedId(Long breedId) {
        this.breedId = breedId;
    }

    public Boolean getSterilized() {
        return sterilized;
    }

    public void setSterilized(Boolean sterilized) {
        this.sterilized = sterilized;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }
    public String getKindOfAnimal() {
        return kindOfAnimal;
    }

    public void setKindOfAnimal(String kindOfAnimal) {
        this.kindOfAnimal = kindOfAnimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return name.equals(pet.name) && birthday.equals(pet.birthday) && gender.equals(pet.gender) && breedId.equals(pet.breedId) && kindOfAnimal.equals(pet.kindOfAnimal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthday, gender, breedId, kindOfAnimal);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", breedId=" + breedId +
                ", sterilized=" + sterilized +
                ", invalid=" + invalid +
                '}';
    }
}
