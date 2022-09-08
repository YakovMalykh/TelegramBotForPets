package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;

@Entity
public class CatAdopter extends Person{
    private Long chatId;


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
