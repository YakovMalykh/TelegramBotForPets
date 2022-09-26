package sky.pro.telegrambotforpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kindOfAnimal;
    private Long petId;
    private Long adopterId;
    private LocalDate adoptionsDate;
    private String adoptionsResult;
    private LocalDate endOfAdoption;
    private String reasonOfEnd;

    @JsonIgnore
    @OneToMany(mappedBy = "adoption")
    private Collection<Report> reports;

    public Adoption() {
    }

    public Adoption(String kindOfAnimal, Long petId, Long adopterId, LocalDate adoptionsDate) {
        this.kindOfAnimal = kindOfAnimal;
        this.petId = petId;
        this.adopterId = adopterId;
        this.adoptionsDate = adoptionsDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindOfAnimal() {
        return kindOfAnimal;
    }

    public void setKindOfAnimal(String kindOfAnimal) {
        this.kindOfAnimal = kindOfAnimal;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Long adopterId) {
        this.adopterId = adopterId;
    }

    public LocalDate getAdoptionsDate() {
        return adoptionsDate;
    }

    public void setAdoptionsDate(LocalDate adoptionsDate) {
        this.adoptionsDate = adoptionsDate;
    }

    public String getAdoptionsResult() {
        return adoptionsResult;
    }

    public void setAdoptionsResult(String adoptionsResult) {
        this.adoptionsResult = adoptionsResult;
    }

    public LocalDate getEndOfAdoption() {
        return endOfAdoption;
    }

    public void setEndOfAdoption(LocalDate endOfAdoption) {
        this.endOfAdoption = endOfAdoption;
    }

    public String getReasonOfEnd() {
        return reasonOfEnd;
    }

    public void setReasonOfEnd(String reasonOfEnd) {
        this.reasonOfEnd = reasonOfEnd;
    }

    public Collection<Report> getReports() {
        return reports;
    }

    public void setReports(Collection<Report> reports) {
        this.reports = reports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adoption adoption = (Adoption) o;
        return id.equals(adoption.id) && kindOfAnimal == adoption.kindOfAnimal && petId.equals(adoption.petId) && adopterId.equals(adoption.adopterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kindOfAnimal, petId, adopterId);
    }

    @Override
    public String toString() {
        return "Adoption {" +
                "id записи об усыновлении = " + id +
                ", вид животного = " + kindOfAnimal +
                ", Id питомца =" + petId +
                ", Id усыновителя =" + adopterId +
                ", Дата усыновления =" + adoptionsDate +
                ", результат усыновления =" + adoptionsResult +
                '}';
    }
}
