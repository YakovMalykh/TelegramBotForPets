package sky.pro.telegrambotforpets.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    @Column(name = "adaptation_id")
    private Long adaptationId;
    @Column(name = "report_date")
    private LocalDate date;
    @Column(name = "photo_path")
    private String photoPath;
    @Column(name = "ration")
    private String ration;
    @Column(name = "behaivor")
    private String behaivor;
    @Column(name = "feeling")
    private String feeling;
    @Column(name = "comment")
    private String comment;

    @ManyToOne
    private Adoption adoption;

    public Report() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdaptationId() {
        return adaptationId;
    }

    public void setAdaptationId(Long adaptationId) {
        this.adaptationId = adaptationId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getRation() {
        return ration;
    }

    public void setRation(String ration) {
        this.ration = ration;
    }

    public String getBehaivor() {
        return behaivor;
    }

    public void setBehaivor(String behaivor) {
        this.behaivor = behaivor;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(adaptationId, report.adaptationId) && Objects.equals(date, report.date) && Objects.equals(photoPath, report.photoPath) && Objects.equals(ration, report.ration) && Objects.equals(behaivor, report.behaivor) && Objects.equals(feeling, report.feeling) && Objects.equals(comment, report.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adaptationId, date, photoPath, ration, behaivor, feeling, comment);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", adaptationId=" + adaptationId +
                ", date=" + date +
                ", photoPath='" + photoPath + '\'' +
                ", ration='" + ration + '\'' +
                ", behaivor='" + behaivor + '\'' +
                ", feeling='" + feeling + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
