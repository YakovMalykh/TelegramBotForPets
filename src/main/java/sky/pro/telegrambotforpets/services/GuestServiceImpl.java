package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.repositories.GuestRepository;

@Service
@Transactional
public class GuestServiceImpl implements GuestService {
    private final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);
    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    /**
     * получение имени пользователя при первом обращении без запроса контакта
     * @param update
     * @return userName или Гость
     */
    public String getUserNameOfGuest(Update update) {
        String userName = update.message().from().username();
        if (userName != null && !(userName.isEmpty() && userName.isBlank())) {
            return userName;
        }
        return "Гость";
    }

    /**
     * сохранение записи в таблицу guests, заносятся поля userName и chatId
     * используется метод репозитория {@link org.springframework.data.jpa.repository.JpaRepository#save(Object)}
     * @param update
     */
    public void saveGuestToDB(Update update) {
        Long chatId = update.message().chat().id();
        String userName = getUserNameOfGuest(update);
        Guest guest = new Guest();
        guest.setChatId(chatId);
        guest.setUserName(userName);
        guestRepository.save(guest);
        logger.info("выполнился метод saveGuestToDB, в БД занесен Гость с именем " + userName + " и chatId " + chatId);
    }


}
