package sky.pro.telegrambotforpets.interfaces;

import com.pengrad.telegrambot.model.Update;

public interface GuestService {
    void saveGuestToDB (Update update);
}
