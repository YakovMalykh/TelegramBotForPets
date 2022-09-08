package sky.pro.telegrambotforpets.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CatAdopter extends Adopter{
    private Long chatId;

    @OneToOne
    private Cat cat;

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
