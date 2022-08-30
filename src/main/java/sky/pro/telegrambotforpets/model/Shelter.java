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
    private Long id;
//  shelter_id BIGINT PRIMARY KEY,

    @Column(name = "shelter_name")
    private String name;
//    shelter_name VARCHAR (40),

    @Column(name = "shelter_adress")
    private String adress;
//    shelter_adress VARCHAR(255),

    @Column(name = "shelter_mappach")
    private String mapPach;
//    shelter_mappach VARCHAR(255),

    @Column(name = "shelter_recomendationpach")
    private String recomendationPach;
//    shelter_recomendationpach VARCHAR(255),

    @Column(name = "shelter_schedule")
    private String schedule;
//    shelter_schedule VARCHAR(255),

    @Column(name = "shelter_specification")
    private String specification;
//    shelter_specification VARCHAR(255),

    @Column(name = "shelter_description")
    private String description;
//    shelter_description VARCHAR(255)


    public Shelter() {
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMapPach() {
        return mapPach;
    }

    public void setMapPach(String mapPach) {
        this.mapPach = mapPach;
    }

    public String getRecomendationPach() {
        return recomendationPach;
    }

    public void setRecomendationPach(String recomendationPach) {
        this.recomendationPach = recomendationPach;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", mapPach='" + mapPach + '\'' +
                ", recomendationPach='" + recomendationPach + '\'' +
                ", schedule='" + schedule + '\'' +
                ", specification='" + specification + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
