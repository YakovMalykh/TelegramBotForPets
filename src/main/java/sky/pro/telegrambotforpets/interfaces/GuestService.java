package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestService {
    /**
     * сохранение записи в таблицу guests, заносятся поля userName и chatId
     * используется метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#save(Object)}
     * @param update
     */
    void saveGuestToDB (Update update);

    void saveContactToDB(Update update);

    /**
     * метод проверяет нет ли уже такого госят в нашей БД
     * @param update - отсюда достаем ID
     * @return true если гость с таким Id уже существует
     * @see JpaRepository#findAll()
     */
    boolean doesGuestAlreadyExistsInDB(Update update);

    SendMessage firstMeeting(Update update);
}
