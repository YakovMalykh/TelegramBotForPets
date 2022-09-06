package sky.pro.telegrambotforpets.menu;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Service;

import static sky.pro.telegrambotforpets.constants.Constants.*;

@Service
public class ReplyKeyboard extends Keyboard {


    public Keyboard Menu0() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(MENU_1_BUTTON_1), new KeyboardButton(MENU_1_BUTTON_2))
                .addRow(new KeyboardButton(MENU_1_BUTTON_3), new KeyboardButton(MENU_1_BUTTON_4))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }

    public Keyboard Menu0_1() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(MENU_1_1_BUTTON_1), new KeyboardButton(MENU_1_1_BUTTON_2))
                .addRow(new KeyboardButton(MENU_1_1_BUTTON_3))
                .addRow(new KeyboardButton(MENU_1_1_BUTTON_4).requestContact(true))
                .addRow(new KeyboardButton(MENU_1_BUTTON_3), new KeyboardButton(MENU_1_BUTTON_4))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }

    public Keyboard Menu0_2() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(new KeyboardButton(MENU_1_2_BUTTON_1))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_2))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_3))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_4))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_5))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_6))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_7))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_8))
                .addRow(new KeyboardButton(MENU_1_2_BUTTON_9))
                .addRow(new KeyboardButton(MENU_1_BUTTON_3), new KeyboardButton(MENU_1_BUTTON_4))
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
        return replyKeyboardMarkup;
    }
}
