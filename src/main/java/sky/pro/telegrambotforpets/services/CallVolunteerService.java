package sky.pro.telegrambotforpets.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatInviteLink;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.CreateChatInviteLink;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.ChatInviteLinkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.model.Volunteer;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.repositories.VolunteerRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class CallVolunteerService {
    private final Logger logger = LoggerFactory.getLogger(CallVolunteerService.class);
    private final VolunteerRepository volunteerRepository;
    private final GuestRepository guestRepository;
    private final TelegramBot bot;

    private int countGCh = 0;
    private int countV = 0;

    public CallVolunteerService(VolunteerRepository volunteerRepository, GuestRepository guestRepository, TelegramBot bot) {
        this.volunteerRepository = volunteerRepository;
        this.guestRepository = guestRepository;
        this.bot = bot;
    }

    /**
     * метод условно рандомно выбирает групповой чат и волонтера, формирует пригласительную ссылку на этот чат
     * и отправляет ее и волонтеру и гостю, переходя по этой ссылке они попадают в один групповой чат
     * и продолжают беседу в нем.
     *
     * @param chatId гостя
     * @see CallVolunteerService#isItGroupChatToTalkWithVolunteer
     */
    public void callVolunteer(Long chatId) {
        CreateChatInviteLink chatInviteLink = new CreateChatInviteLink(chooseGroupChat());
        Long expireDate = Instant.now().getEpochSecond() + 3600;//получаю время данного момента в Unix формате
        // и прибавляю один час - это время существования ссылки
        CreateChatInviteLink createChatInviteLink = chatInviteLink.name("chat with volunteer")
                .expireDate(expireDate.intValue());

        ChatInviteLinkResponse response = bot.execute(createChatInviteLink);
        ChatInviteLink link = response.chatInviteLink();
        //достаю из ответа ссылку ввиде строки
        String inviteLink1 = link.inviteLink();

        // идея в том чтобы иметь несколько предсозданных групповых чатов, в которых состоит бот в
        // качестве администратора. И мы в один из них приглашаем и гостя и волонтера.

        // отправляю ссылку в чат гостя позвавшего волонтера
        bot.execute(new SendMessage(chatId, "для беседы с волонтером перейдите по ссылке \n"
                + inviteLink1));
        // отправляю ту же ссылку волонтеру
        bot.execute(new SendMessage(getVolonteer(), "для беседы с гостем перейдите по ссылке \n"
                + inviteLink1));

    }

    /**
     * т.к. групповые чаты начианются с "-", а любой чат ботом сохраняется как "Гость",
     * мы находим в БД гостей все имеющиеся чаты начинающиеся с "-" и по очереди обращаемся к каждому из них
     * при новом вызове метода
     *
     * @return строку с chatId группового чата
     */
    private String chooseGroupChat() {
        //здесь хочу в базе госетей найти все групповые чаты и выбрать какой-нибудь из них
        List<Long> allGroupChats = guestRepository.findAll().stream().filter(
                        e -> e.getChatId().toString().startsWith("-"))
                .toList()
                .stream().map(Guest::getChatId).toList();

        String groupChatIdString = allGroupChats.get(countGCh).toString();
        countGCh++;
        if (countGCh == allGroupChats.size()) {
            countGCh = 0;
        }
        return groupChatIdString;
    }

    /**
     * метод по очереди берет волонтеров из БД волонтеров
     *
     * @return строку с chatId волонтера
     */
    private String getVolonteer() {
        //здесь хочу реализовать рандомный(или как-то по порядку) выбор волонтера из списка
//        volunteerRepository.findById(1L).get().getVolunteerChatId().toString();
        List<Long> all = volunteerRepository.findAll().stream()
                .map(Volunteer::getVolunteerChatId).sorted().toList();

        String volunteerChatIdString = all.get(countV).toString();

        countV++;
        if (countV == all.size()) {
            countV = 0;
        }
        return volunteerChatIdString;
    }

    /**
     * проверяет по chatId является ли этот чат групповым, если это групповой чат, то бот должен игнорировать
     * все сообщения которые в нем отправляются
     *
     * @param update
     * @return true если это групповой чат для разговора с волонтером
     */
    public boolean isItGroupChatToTalkWithVolunteer(Update update) {
        return update.message().chat().id().toString().startsWith("-");
    }
}
