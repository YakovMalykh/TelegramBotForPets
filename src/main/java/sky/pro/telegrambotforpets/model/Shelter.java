package sky.pro.telegrambotforpets.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность <b>Shelter</b> содержит информация о приюте. <br>
 * Отображается на таблицу <b>shelter</b>
 */
@Entity
@Table(name = "shelter")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_id")
    private Long id;
//  shelter_id BIGINT PRIMARY KEY,

    @Column(name = "shelter_name")
    private String name;
//    shelter_name VARCHAR (40),

    @Column(name = "shelter_adress")
    private String adress;
//    shelter_adress VARCHAR(255),

    @Column(name = "shelter_mappath")
    private String mapPath;
//    shelter_mappath VARCHAR(255),

    /**
     * имеются в виду правила безопасности
     */
    @Column(name = "shelter_recomendationpath")
    private String recomendationPath;
//    shelter_recomendationpath VARCHAR(255),

    @Column(name = "shelter_schedule")
    private String schedule;
//    shelter_schedule VARCHAR(255),

    @Column(name = "shelter_specialization")
    private String specialization;
//    shelter_specialization VARCHAR(255),

    @Column(name = "shelter_description")
    private String description;
    //    shelter_description VARCHAR(255)
    @Column(name = "shelter_security_phone_number")
    private String securityPhoneNumber;

    public Shelter() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("передан null в качестве параметра");
        }
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        if (adress != null) {
            this.adress = adress;
        } else {
            throw new IllegalArgumentException("передан null в качестве параметра");
        }
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getRecomendationPath() {
        return recomendationPath;
    }

    public void setRecomendationPath(String recomendationPath) {
        this.recomendationPath = recomendationPath;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        if (schedule != null) {
            this.schedule = schedule;
        } else {
            throw new IllegalArgumentException("передан null в качестве параметра");
        }
    }

    public String getSpecialization() {
        return specialization;
    }

    /**
     * допустимыпе значения DOGS, CATS
     *
     * @param specialization
     */
    public void setSpecialization(String specialization) {
        if (specialization != null) {
            if (specialization.equalsIgnoreCase("DOGS") ||
                    specialization.toUpperCase().equals("CATS")) {
                this.specialization = specialization.toUpperCase();
            } else {
                throw new IllegalArgumentException("переданы некорректные параметры");
            }
        } else {
            throw new IllegalArgumentException("передан null в качестве параметра");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("передан null в качестве параметра");
        }
    }

    public String getSecurityPhoneNumber() {
        return securityPhoneNumber;
    }

    /**
     * сохраняем в БД номер телефона вида 89990001122
     *
     * @param securityPhoneNumber
     */
    public void setSecurityPhoneNumber(String securityPhoneNumber) {
        if (securityPhoneNumber != null && !(securityPhoneNumber.isBlank() && securityPhoneNumber.isEmpty())) {
            this.securityPhoneNumber = securityPhoneNumber
                    .replace("+7", "8")
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", "");
        } else {
            throw new IllegalArgumentException("введены некорректные данные");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return id.equals(shelter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", mapPath='" + mapPath + '\'' +
                ", recomendationPath='" + recomendationPath + '\'' +
                ", schedule='" + schedule + '\'' +
                ", specialization='" + specialization + '\'' +
                ", description='" + description + '\'' +
                ", securityPhoneNumber='" + securityPhoneNumber + '\'' +
                '}';
    }
}
