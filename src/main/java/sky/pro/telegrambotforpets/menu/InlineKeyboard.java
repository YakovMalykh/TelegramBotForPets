package sky.pro.telegrambotforpets.menu;

import com.pengrad.telegrambot.model.request.*;
import org.springframework.stereotype.Service;
import sky.pro.telegrambotforpets.constants.Buttons;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.CatAdopterRepository;
import sky.pro.telegrambotforpets.repositories.DogAdopterRepository;


@Service
public class InlineKeyboard extends Keyboard {
    private final CatAdopterRepository catAdopterRepository;
    private final DogAdopterRepository dogAdopterRepository;

    public InlineKeyboard(CatAdopterRepository catAdopterRepository, DogAdopterRepository dogAdopterRepository) {
        this.dogAdopterRepository = dogAdopterRepository;
        this.catAdopterRepository = catAdopterRepository;
    }

    public Keyboard Menu() {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Buttons.MENU_0_BUTTON_1.getButtonName()).callbackData(Buttons.MENU_0_BUTTON_1.name()), new InlineKeyboardButton(Buttons.MENU_0_BUTTON_2.getButtonName()).callbackData(Buttons.MENU_0_BUTTON_2.name()));
        return replyKeyboardMarkup;
    }
  public Keyboard MenuReport(Long shelterId) {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Buttons.MENU_1_3_BUTTON_1.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_3_BUTTON_1.name()),
                new InlineKeyboardButton(Buttons.MENU_1_3_BUTTON_2.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_3_BUTTON_2.name()))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_3_BUTTON_3.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_3_BUTTON_3.name()),
                        new InlineKeyboardButton(Buttons.MENU_1_3_BUTTON_4.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_3_BUTTON_4.name()))
                .addRow(new InlineKeyboardButton(Buttons.MENU_EXIT.getButtonName()).callbackData(Buttons.MENU_EXIT.name()));
        return replyKeyboardMarkup;
    }

    public Keyboard MenuCommon(Shelter shelter, Long chatId) {
        Long shelterId = shelter.getId();
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Buttons.MENU_1_BUTTON_1.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_BUTTON_1),
                new InlineKeyboardButton(Buttons.MENU_1_BUTTON_2.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_BUTTON_2))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_BUTTON_4.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_BUTTON_4));
        if ((catAdopterRepository.findByChatId(chatId).isPresent() && shelter.getSpecialization().equals(KindOfAnimal.CATS.name())) || (dogAdopterRepository.findByChatId(chatId).isPresent() && shelter.getSpecialization().equals(KindOfAnimal.DOGS.name()))) {
            replyKeyboardMarkup.addRow(new InlineKeyboardButton(Buttons.MENU_1_BUTTON_3.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_BUTTON_3));
        }
        return replyKeyboardMarkup;
    }

    public Keyboard MenuAboutShelter(Long shelterId) {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Buttons.MENU_1_1_BUTTON_1.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_1_BUTTON_1), new InlineKeyboardButton(Buttons.MENU_1_1_BUTTON_2.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_1_BUTTON_2))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_1_BUTTON_3.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_1_BUTTON_3))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_1_BUTTON_4.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_1_BUTTON_4))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_1_BUTTON_5.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_1_BUTTON_5));
        return replyKeyboardMarkup;
    }

    public Keyboard MenuContact() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Buttons.MENU_1_BUTTON_5.getButtonName()).requestContact(true))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }

    public Keyboard MenuHowGetPet(Shelter shelter) {
        Long shelterId = shelter.getId();
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_1.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_1))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_2.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_2))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_3.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_3))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_4.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_4))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_5.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_5))
                .addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_6.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_6));
        if (shelter.getSpecialization().equals(KindOfAnimal.DOGS.name())) {
            replyKeyboardMarkup.addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_7.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_7))
                    .addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_8.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_8));
        }
        replyKeyboardMarkup.addRow(new InlineKeyboardButton(Buttons.MENU_1_2_BUTTON_9.getButtonName()).callbackData(shelterId.toString() + "/" + Buttons.MENU_1_2_BUTTON_9));
        Keyboard inlineKeyboardMarkup = replyKeyboardMarkup;
        return replyKeyboardMarkup;
    }
}
