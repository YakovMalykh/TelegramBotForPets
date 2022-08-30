package sky.pro.telegrambotforpets.model;

import javax.persistence.*;
import java.util.Objects;
/**
 * Сущность <b>Preparation</b> содержит информацию о правилах, советах
 * и рекомендациях для подготовки с питовцем. <br>
 * Отображается на таблицу <b>preparation</b>
 */
@Entity
@Table(name = "preparation")
public class Preparation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    preparation_id BIGINT PRIMARY KEY,

    @Column(name = "preparation_daitingrulespath")
    private String daitingRulesPath;
//    preparation_daitingrulespath VARCHAR (255),

    @Column(name = "preparation_requiredadoptpath")
    private String requiredAdoptPath;
//    preparation_requiredadoptpath VARCHAR (255),

    @Column(name = "preparation_reccomtranstortpath")
    private String reccomTranstortPath;
//    preparation_reccomtranstortpath VARCHAR (255),

    @Column(name = "preparation_homeimprovementpuppypath")
    private String homeImprovementPuppyPath;
//    preparation_homeimprovementpuppypath VARCHAR (255),

    @Column(name = "preparation_homeimprovementdogpath")
    private String homeImprovementDogPath;
//    preparation_homeimprovementdogpath VARCHAR (255),

    @Column(name = "preparation_doghandlersadvisepath")
    private String dogHandlersAdvisePath;
//    preparation_doghandlersadvisepath VARCHAR (255),

    @Column(name = "preparation_reccomdoghandlerspath")
    private String reccomDogHandlersPath;
//    preparation_reccomdoghandlerspath VARCHAR (255),

    @Column(name = "preparation_reasonsrefusingpath")
    private String reasonsRefusingPath;
//    preparation_reasonsrefusingpath VARCHAR (255),

    @Column(name = "preparation_reccomdisabilitiesgpath")
    private String reccomDisabilitiesgPath;
//    preparation_reccomdisabilitiesgpath VARCHAR (255)


    public Preparation() {
    }

    public Long getId() {
        return id;
    }

    public String getDaitingRulesPath() {
        return daitingRulesPath;
    }

    public void setDaitingRulesPath(String daitingRulesPath) {
        this.daitingRulesPath = daitingRulesPath;
    }

    public String getRequiredAdoptPath() {
        return requiredAdoptPath;
    }

    public void setRequiredAdoptPath(String requiredAdoptPath) {
        this.requiredAdoptPath = requiredAdoptPath;
    }

    public String getReccomTranstortPath() {
        return reccomTranstortPath;
    }

    public void setReccomTranstortPath(String reccomTranstortPath) {
        this.reccomTranstortPath = reccomTranstortPath;
    }

    public String getHomeImprovementPuppyPath() {
        return homeImprovementPuppyPath;
    }

    public void setHomeImprovementPuppyPath(String homeImprovementPuppyPath) {
        this.homeImprovementPuppyPath = homeImprovementPuppyPath;
    }

    public String getHomeImprovementDogPath() {
        return homeImprovementDogPath;
    }

    public void setHomeImprovementDogPath(String homeImprovementDogPath) {
        this.homeImprovementDogPath = homeImprovementDogPath;
    }

    public String getDogHandlersAdvisePath() {
        return dogHandlersAdvisePath;
    }

    public void setDogHandlersAdvisePath(String dogHandlersAdvisePath) {
        this.dogHandlersAdvisePath = dogHandlersAdvisePath;
    }

    public String getReccomDogHandlersPath() {
        return reccomDogHandlersPath;
    }

    public void setReccomDogHandlersPath(String reccomDogHandlersPath) {
        this.reccomDogHandlersPath = reccomDogHandlersPath;
    }

    public String getReasonsRefusingPath() {
        return reasonsRefusingPath;
    }

    public void setReasonsRefusingPath(String reasonsRefusingPath) {
        this.reasonsRefusingPath = reasonsRefusingPath;
    }

    public String getReccomDisabilitiesgPath() {
        return reccomDisabilitiesgPath;
    }

    public void setReccomDisabilitiesgPath(String reccomDisabilitiesgPath) {
        this.reccomDisabilitiesgPath = reccomDisabilitiesgPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preparation that = (Preparation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Preparation{" +
                "id=" + id +
                ", daitingRulesPath='" + daitingRulesPath + '\'' +
                ", requiredAdoptPath='" + requiredAdoptPath + '\'' +
                ", reccomTranstortPath='" + reccomTranstortPath + '\'' +
                ", homeImprovementPuppyPath='" + homeImprovementPuppyPath + '\'' +
                ", homeImprovementDogPath='" + homeImprovementDogPath + '\'' +
                ", dogHandlersAdvisePath='" + dogHandlersAdvisePath + '\'' +
                ", reccomDogHandlersPath='" + reccomDogHandlersPath + '\'' +
                ", reasonsRefusingPath='" + reasonsRefusingPath + '\'' +
                ", reccomDisabilitiesgPath='" + reccomDisabilitiesgPath + '\'' +
                '}';
    }
}
