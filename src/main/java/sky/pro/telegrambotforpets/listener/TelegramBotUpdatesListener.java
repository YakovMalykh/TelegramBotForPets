package sky.pro.telegrambotforpets.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.interfaces.GuestService;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.services.GuestServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * отвечает за работу телеграм бота
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    @Autowired
    private TelegramBot telegramBot;

    private final GuestServiceImpl guestService;
    private final GuestRepository guestRepository;

    public TelegramBotUpdatesListener(GuestServiceImpl guestService, GuestRepository guestRepository) {
        this.guestService = guestService;
        this.guestRepository = guestRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            /**
             * здесь пока просто отвечает "Привет!" на любое сообщение
             *
             */
            /**
             * занесение данных Гостя в базу при первом обращении
             */
            Long chatId = update.message().chat().id();
            String userName = guestService.getUserNameOfGuest(update);
            Guest guest = new Guest();
            guest.setChatId(chatId);
            guest.setUserName(userName);
            guestRepository.save(guest);

            SendMessage message = new SendMessage(chatId, "Привет!");
            SendResponse responce = telegramBot.execute(message);
            System.out.println(responce.isOk());
            System.out.println(responce.errorCode());

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
