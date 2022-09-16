package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
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
     *
     * @param update
     * @return userName или Гость
     */
    public String getUserNameOfGuest(Update update) {
        String userName = update.message().from().username();
        String firstName = update.message().from().firstName();
        if (userName != null && !(userName.isEmpty() && userName.isBlank())) {
            return userName;
        } else if (firstName != null && !(firstName.isEmpty() && firstName.isBlank())) {
            return firstName;
        }
        return "Гость";
    }

    @Override
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

    @Override
    /**
     * метод проыверяет нет ли такого госят уже в нашей БД
     * @param update - отсюда достаем ID
     * @return true если гость с таким Id уже существует
     * @see JpaRepository#findAll()
     */
    public boolean doesGuestAlreadyExistsInDB(Update update) {
        Long chatId = update.message().chat().id();
        logger.info("выполнился метод doesGuestAlreadyExistsInDB");
        return guestRepository.findById(chatId).isPresent();
    }

    @Override
    public SendMessage firstMeeting(Update update) {
        logger.info("выведено приветсвие при первом визите");
        return new SendMessage(update.message().chat().id(), "Приветствую тебя! " +
                "Я - бот приюта кошек и собак. Я могу ответить почти на все твои вопросы!");
    }
}
