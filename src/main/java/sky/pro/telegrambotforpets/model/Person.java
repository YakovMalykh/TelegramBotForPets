package sky.pro.telegrambotforpets.model;

import sky.pro.telegrambotforpets.constants.Gender;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


/**
 * вводим абстаркатный класс, чтобы впоследствии от него можно было наследовать Усыновителя,
 * Кинолога и, возможно, Волонтера
 */
@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate birthday;
    private String phoneNumber;
    private String address;

    public Person() {
    }

    public Person(String name, String middleName, String lastName, String gender,
                  LocalDate birthday, String phoneNumber, String address) {
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        setPhoneNumber(phoneNumber);
        this.address = address;
    }

    public Long getId() {
        return id;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
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
        if (phoneNumber != null && !(phoneNumber.isBlank() && phoneNumber.isEmpty())) {

            this.phoneNumber = phoneNumber
                    .replace("+7", "8")
                    .replace("7","8")
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
        return name.equals(person.name) && middleName.equals(person.middleName) && lastName.equals(person.lastName) && gender.equals(person.gender) && birthday.equals(person.birthday) && phoneNumber.equals(person.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, middleName, lastName, gender, birthday, phoneNumber);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
