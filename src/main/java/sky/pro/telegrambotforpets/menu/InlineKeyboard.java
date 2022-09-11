package sky.pro.telegrambotforpets.menu;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Service;

import static sky.pro.telegrambotforpets.constants.Constants.*;

@Service
public class InlineKeyboard extends Keyboard {
    public Keyboard Menu() {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(MENU_0_BUTTON_1).callbackData(MENU_0_BUTTON_1), new InlineKeyboardButton(MENU_0_BUTTON_2).callbackData(MENU_0_BUTTON_2));

        return replyKeyboardMarkup;
    }

    public Keyboard MenuTest() {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Тест").callbackData("Тест12"));

        return replyKeyboardMarkup;
    }

  /*  переделать кнопки из-за ограничения на количество символов в каллбекдата
*/
    public Keyboard MenuCommon(Long shelterId) {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(MENU_1_BUTTON_1).callbackData(shelterId.toString() + "/" + MENU_1_BUTTON_1), new InlineKeyboardButton(MENU_1_BUTTON_2).callbackData(shelterId.toString() + "/" + MENU_1_BUTTON_2))
                .addRow(new InlineKeyboardButton(MENU_1_BUTTON_3).callbackData(shelterId.toString() + "/" + MENU_1_BUTTON_3), new InlineKeyboardButton(MENU_1_BUTTON_4).callbackData(shelterId.toString() + "/" + MENU_1_BUTTON_4));
        return replyKeyboardMarkup;
    }

    public Keyboard MenuAboutShelter(Long shelterId) {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(MENU_1_1_BUTTON_1).callbackData(shelterId.toString() + "/" + MENU_1_1_BUTTON_1), new InlineKeyboardButton(MENU_1_1_BUTTON_2).callbackData(shelterId.toString() + "/" + MENU_1_1_BUTTON_2))
                .addRow(new InlineKeyboardButton(MENU_1_1_BUTTON_3).callbackData(shelterId.toString() + "/" + MENU_1_1_BUTTON_3))
                .addRow(new InlineKeyboardButton(MENU_1_1_BUTTON_4).callbackData(shelterId.toString() + "/" + MENU_1_1_BUTTON_4))
                .addRow(new InlineKeyboardButton(MENU_1_1_BUTTON_5).callbackData(shelterId.toString() + "/" + MENU_1_1_BUTTON_5))
                .addRow(new InlineKeyboardButton(MENU_1_BUTTON_5).callbackData(shelterId.toString() + "/" + MENU_1_BUTTON_5),new InlineKeyboardButton(MENU_1_BUTTON_4).callbackData(MENU_1_BUTTON_4));
        return replyKeyboardMarkup;
    }

    public Keyboard MenuHowGetPet(Long shelterId) {
        Keyboard replyKeyboardMarkup = new InlineKeyboardMarkup(new InlineKeyboardButton(MENU_1_2_BUTTON_1).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_1))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_2).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_2))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_3).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_3))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_4).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_4))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_5).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_5))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_6).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_6))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_7).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_7))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_8).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_8))
                .addRow(new InlineKeyboardButton(MENU_1_2_BUTTON_9).callbackData(shelterId.toString() + "/" +MENU_1_2_BUTTON_9));
                 return replyKeyboardMarkup;
    }
}
