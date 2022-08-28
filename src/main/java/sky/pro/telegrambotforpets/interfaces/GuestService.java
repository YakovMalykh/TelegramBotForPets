package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.Update;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestService {
    /**
     * сохранение записи в таблицу guests, заносятся поля userName и chatId
     * используется метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#save(Object)}
     * @param update
     */
    void saveGuestToDB (Update update);
    /**
     * метод проверяет нет ли уже такого госят в нашей БД
     * @param update - отсюда достаем ID
     * @return true если гость с таким Id уже существует
     * @see JpaRepository#findAll()
     */
    public boolean doesGuestAlreadyExistsInDB(Update update);
}
