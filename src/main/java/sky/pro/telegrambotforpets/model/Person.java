package sky.pro.telegrambotforpets.model;

import java.util.Date;
import java.util.Objects;


/**
 * вводим абстаркатный класс, чтобы впоследствии от него можно было наследовать Усыновителя,
 * Кинолога и, возможно, Волонтера
 */
public abstract class Person {
    private String name;
    private String middleName;
    private String lastName;
    private String gender;
    private Date birthday;
    private String phoneNumber;
    private String address;

    public Person() {
    }

    public Person(String name, String middleName, String lastName, String gender,
                  Date birthday, String phoneNumber, String address) {
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    /**
     * здесь ввел Enum, т.к. думаю возможны и другие варианты обозначения пола: "W", "Ж", "М".
     * если вводить их, то Enum будет удобен
     */
    private enum Gender {
        M, F
    }

    /**
     * проверяю, что только один символ и он может быть
     * только M или F
     * @param gender
     * @throws IllegalArgumentException
     */
    public void setGender(String gender) {
        if (gender.length() == 1 && (
                gender.toUpperCase().equals(Gender.M.name()) || gender.toUpperCase().equals(Gender.F.name())
        )) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("введены недопустимые данные");
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * сохраняем в БД номер телефона вида 89990001122
     *
     * @param phoneNumber
     * @throws IllegalArgumentException
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !(phoneNumber.isBlank() && phoneNumber.isBlank())) {

            this.phoneNumber = phoneNumber
                    .replace("+7", "8")
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", "");
        } else {
            throw new IllegalArgumentException("введены некорректные данные");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(middleName, person.middleName) && Objects.equals(lastName, person.lastName) && Objects.equals(gender, person.gender) && Objects.equals(birthday, person.birthday) && Objects.equals(phoneNumber, person.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, middleName, lastName, gender, birthday, phoneNumber);
    }

    @Override
    public String toString() {
        return " " + "name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address;
    }
}
