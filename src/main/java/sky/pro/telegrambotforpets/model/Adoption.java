package sky.pro.telegrambotforpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import sky.pro.telegrambotforpets.constants.AdoptionsResult;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long petId;
    private Long adopterId;
    private LocalDate adoptionsDate;
    private AdoptionsResult adoptionsResult;
    private LocalDate endOfAdoption;
    private String reasonOfEnd;

    @JsonIgnore
    @OneToMany(mappedBy = "adoption")
    private Collection<Report> reports;

    public Adoption() {
    }

    public Long getId() {
        return id;
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

    public AdoptionsResult getAdoptionsResult() {
        return adoptionsResult;
    }

    public void setAdoptionsResult(AdoptionsResult adoptionsResult) {
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
        return id.equals(adoption.id) && petId.equals(adoption.petId) && adopterId.equals(adoption.adopterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, petId, adopterId);
    }

    @Override
    public String toString() {
        return "Adoption{" +
                "id=" + id +
                ", petId=" + petId +
                ", adopterId=" + adopterId +
                ", adoptionsDate=" + adoptionsDate +
                ", adoptionsResult=" + adoptionsResult +
                ", endOfAdoption=" + endOfAdoption +
                ", reasonOfEnd='" + reasonOfEnd + '\'' +
                ", reports=" + reports +
                '}';
    }
}
