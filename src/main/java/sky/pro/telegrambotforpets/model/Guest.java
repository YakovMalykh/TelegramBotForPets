package sky.pro.telegrambotforpets.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * создана сущность Guest, нужна для первичной коммуникации
 * подключил к таблице guests
 */
@Entity
@Table(name = "guests")
public class Guest {
    @Id

    private Long chatId;
    private String userName;
    private String phoneNumber;

    public Guest() {
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber
                .replace("+7", "8")
                .replace("7","8")
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return chatId.equals(guest.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
