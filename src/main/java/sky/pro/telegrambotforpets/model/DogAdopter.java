package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class DogAdopter extends Person{

    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

}
