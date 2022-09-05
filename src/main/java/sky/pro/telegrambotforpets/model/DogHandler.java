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
public class DogHandler extends Person {

    private Long idChat;
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
        if (!super.equals(o)) return false;
        DogHandler that = (DogHandler) o;
        return Objects.equals(idChat, that.idChat) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idChat, description);
    }

    @Override
    public String toString() {
        return "DogHandler{" +
                "idChat=" + idChat +
                ", description='" + description + '\'' +
                '}'+ super.toString();
    }
}
