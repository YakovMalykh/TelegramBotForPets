package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long volunteerId;

    private Long volunteerChatId;
    private String volunteerName;
    private String volunteerPhoneNumber;

    public Volunteer() {
    }

    public Volunteer(String volunteerName, String volunteerPhoneNumber) {
        this.volunteerName = volunteerName;
        this.volunteerPhoneNumber = volunteerPhoneNumber;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public Long getVolunteerChatId() {
        return volunteerChatId;
    }

    public void setVolunteerChatId(Long volunteerChatId) {
        this.volunteerChatId = volunteerChatId;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getVolunteerPhoneNumber() {
        return volunteerPhoneNumber;
    }

    public void setVolunteerPhoneNumber(String volunteerPhoneNumber) {
        this.volunteerPhoneNumber = volunteerPhoneNumber
                .replace("+7", "8")
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return volunteerId.equals(volunteer.volunteerId) && volunteerPhoneNumber.equals(volunteer.volunteerPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(volunteerId, volunteerPhoneNumber);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "volunteerId=" + volunteerId +
                ", volunteerChatId=" + volunteerChatId +
                ", volunteerName='" + volunteerName + '\'' +
                ", volunteerPhoneNumber='" + volunteerPhoneNumber + '\'' +
                '}';
    }
}
