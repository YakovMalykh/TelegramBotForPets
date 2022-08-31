package sky.pro.telegrambotforpets.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * Сущность <b>DogHandler</b> содержит информация о кинологе. <br>
 * Отображается на таблицу <b>doghandler</b>
 */
@Entity
//@Table(name = "doghandler")
public class DogHandler extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idChat;
    /**
     * объявляю здесь все поля родительского класса, иначе они не создаются в таблице
     */
    private String name;

    private String middleName;

    private String lastName;

    private String gender;

    private Date birthday;

    private String phoneNumber;

    private String address;
    private String description;

    public DogHandler() {
    }

    public DogHandler(String name, String middleName, String lastName, String gender,
                      Date birthday, String phoneNumber, String address, Long idChat,
                      String description) {
        super(name, middleName, lastName, gender, birthday, phoneNumber, address);
        this.idChat = idChat;
        this.description = description;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
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
        DogHandler that = (DogHandler) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DogHandler{" +
                "id=" + id +
                ", idChat=" + idChat +
                ", description='" + description + '\'' +
                '}' + super.toString();
    }

}
