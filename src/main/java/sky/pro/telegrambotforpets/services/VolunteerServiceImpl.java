package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.interfaces.VolunteerService;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.model.Volunteer;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.repositories.VolunteerRepository;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class VolunteerServiceImpl implements VolunteerService {

    private final Logger logger = LoggerFactory.getLogger(VolunteerServiceImpl.class);
    private final VolunteerRepository volunteerRepository;
    private final GuestRepository guestRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository, GuestRepository guestRepository) {
        this.volunteerRepository = volunteerRepository;
        this.guestRepository = guestRepository;
    }


    @Override
    public boolean saveVolunteerToDB(String name, String phoneNumber) {

        try {
            Volunteer volunteer = new Volunteer(name, phoneNumber);
            // пытаемся достать из БД по этому номеру телефона гостя, чтобы получить его chatId,
            // если гостя с таким тел. нет - ошибка и предложение сначала создать чат в качестве гостя
            // и поделиться контактом
            Guest guest = guestRepository.findByPhoneNumber(volunteer.getVolunteerPhoneNumber()).get();
            volunteer.setVolunteerChatId(guest.getChatId());
            volunteerRepository.save(volunteer);
            logger.info("Волонтер сохранен в БД");
            return true;
        } catch (NoSuchElementException e) {
            logger.info(e.toString());
            logger.info("перед занесением волонтера в Бд, он должен создать свой чат с ботом и отправить ему conact");
            return false;
        }
    }
}
