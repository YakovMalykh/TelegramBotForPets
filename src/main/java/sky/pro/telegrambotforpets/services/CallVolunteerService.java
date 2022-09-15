package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.ChatInviteLink;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.CreateChatInviteLink;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.ChatInviteLinkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.repositories.VolunteerRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class CallVolunteerService {
    private final Logger logger = LoggerFactory.getLogger(CallVolunteerService.class);
    private final VolunteerRepository volunteerRepository;
    private final GuestRepository guestRepository;
    private final TelegramBot bot;

    public CallVolunteerService(VolunteerRepository volunteerRepository, GuestRepository guestRepository, TelegramBot bot) {
        this.volunteerRepository = volunteerRepository;
        this.guestRepository = guestRepository;
        this.bot = bot;
    }

    public void callVolunteer(Long chatId, Update update) {
//        SendMessage callsVolunteer = new SendMessage("-1001534654244", "User calls volunteer ");
//        bot.execute(callsVolunteer);
        //здесь вставляю пока руками chatId групповго чата они начинаются со -100 или -...
        CreateChatInviteLink chatInviteLink = new CreateChatInviteLink("-1001534654244");
        Long expireDate = Instant.now().getEpochSecond() + 3600;//получаю время данного момента в Unix формате
        // и прибавляю один час - это время существования ссылки
        CreateChatInviteLink createChatInviteLink = chatInviteLink.name("chat with volunteer")
                .expireDate(expireDate.intValue());

        ChatInviteLinkResponse response = bot.execute(createChatInviteLink);
        ChatInviteLink link = response.chatInviteLink();
        //достаю из ответа ссылку ввиде строки
        String inviteLink1 = link.inviteLink();

        // идея в том чтобы иметь несколько предсозданных групповых чатов, в которых состоит один бот и
        // какой-ниб администратор. И мы в один из них приглашаем и гостя и волонтера. Т.е волонтер может
        // и не состоять в этом чата до этого

        // отправляю ссылку в чат гостя позвавшего волонтера
        bot.execute(new SendMessage(chatId, inviteLink1));
        // отправляю ту же ссылку волонтеру
        bot.execute(new SendMessage(getVolonteer(),inviteLink1));

    }

    private String findEmptyGroupChat() {
        //здесь хочу в базе госетей найти все групповые чаты и выбрать какой-нибудь из них
        List<Long> allGroupChats = guestRepository.findAll().stream().filter(
                        e -> e.getChatId().toString().startsWith("-"))
                .toList()
                .stream().map(Guest::getChatId).toList();
        return null;
    }
    private String getVolonteer() {
        //здесь хочу реализовать рандомный(или как-то по порядку) выбор волонтера из списка
       return volunteerRepository.findById(1L).get().getVolunteerChatId().toString();
    }

}
