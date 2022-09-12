package sky.pro.telegrambotforpets.constants;

public enum Buttons {
    /*
    Начальное меню (этап 0)
     */
    MENU_1_BUTTON_1("Информация о приюте"),
    MENU_1_BUTTON_2("Как взять питомца из приюта"),
    MENU_1_BUTTON_3("Отправить отчет о питомце"),
    MENU_1_BUTTON_4("Позвать волонтера"),
    MENU_1_BUTTON_5("Оставить контакт"),
    /*
   Вложенное меню (этап 1)
    */
    MENU_1_1_BUTTON_1("Описание приюта"),
    MENU_1_1_BUTTON_2("Время работы"),
    MENU_1_1_BUTTON_3("Адрес приюта"),
    MENU_1_1_BUTTON_4("Правила безопасности на территории приюта"),
    MENU_1_1_BUTTON_5("Телефон охраны для оформления пропуска"),

    /*
  Вложенное меню (этап 2)
   */
    MENU_1_2_BUTTON_1("Правила знакомства с питомцем"),
    MENU_1_2_BUTTON_2("Документы, чтобы взять питомца"),
    MENU_1_2_BUTTON_3("Рекомендации по транспортировке"),
    MENU_1_2_BUTTON_4("Обустройство дома для щенка/котенка"),
    MENU_1_2_BUTTON_5("Обустройство дома для взрослого питомца"),
    MENU_1_2_BUTTON_6("ДОбустройство дома для взрослого питомца с ограниченными возможностями"),
    MENU_1_2_BUTTON_7("Советы кинолога"),
    MENU_1_2_BUTTON_8("Проверенные кинологи"),
    MENU_1_2_BUTTON_9("Причины отказа в заборе питомца"),

    MENU_0_BUTTON_1("Приют для собак"),
    MENU_0_BUTTON_2("Приют для кошек"),

    MENU_VOLANTER("Принять запрос"),

    MENU_0_BUTTON_0("Нет такой команды");


    private final String buttonName;

    private Buttons(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return this.buttonName;
    }

    //  public static final Buttons getByValue(String buttonName) {
    //  return Arrays.stream(Buttons.values()).filter(e -> e.getButtonName().equals(buttonName)).findFirst().orElse(MENU_0_BUTTON_0);
    //}
}

