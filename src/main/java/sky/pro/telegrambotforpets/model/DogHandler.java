package sky.pro.telegrambotforpets.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * Сущность <b>DogHandler</b> содержит информация о кинологе. <br>
 * Отображается на таблицу <b>doghandler</b>
 */
@Entity
@Table(name = "doghandler")
public class DogHandler extends Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doghandler_chatid")
    private Long idChat;
    // doghandler_chatid BIGINT;

    @Column(name = "doghandler_name")
    private String name;
    //doghandler_name VARCHAR (20),

    @Column(name = "doghandler_middlename")
    private String middleName;
//    doghandler_middlename VARCHAR (20),

    @Column(name = "doghandler_lastname")
    private String lastName;
//    doghandler_lastname VARCHAR (20),

    @Column(name = "doghandler_gender")
    private String gender;
//    doghandler_gender VARCHAR (1),

    @Column(name = "doghandler_datebirth")
    private Date birthday;
//    doghandler_datebirth DATE,

    @Column(name = "doghandler_telephone")
    private String phoneNumber;
//    doghandler_telephone VARCHAR (20),

    @Column(name = "doghandler_adress")
    private String address;
//    doghandler_adress VARCHAR (255),

    @Column(name = "doghandler_description")
    private String description;
//    doghandler_description VARCHAR (255)


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogHandler that = (DogHandler) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
