package sky.pro.telegrambotforpets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.controllers.AdopterController;
import sky.pro.telegrambotforpets.model.CatAdopter;
import sky.pro.telegrambotforpets.model.DogAdopter;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.repositories.CatAdopterRepository;
import sky.pro.telegrambotforpets.repositories.DogAdopterRepository;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.services.AdopterServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdopterController.class)
public class TelegramBotForPetsApplicationAdopterContrTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DogAdopterRepository dogAdopterRepository;
    @MockBean
    private CatAdopterRepository catAdopterRepository;
    @MockBean
    private GuestRepository guestRepository;
    @SpyBean
    private AdopterServiceImpl adopterServiceImpl;

    @InjectMocks
    private AdopterController adopterController;

    private final String testName = "NameTest";

    private final String testMiddleName = "MiddleNameTest";
    private final String testLastName = "LastName";
    private final String gender = Gender.M.name();
    private final String birthDate = "22.12.2000";
    private final String phoneNumber = "81112224466";
    private final String address = "Moscow";
    private final String kindOfAnimalCat = KindOfAnimal.CATS.name();
    private List<DogAdopter> dogAdopterList = new ArrayList<>();
    private List<CatAdopter> catAdopterList = new ArrayList<>();
    private Guest guest = new Guest();
    private CatAdopter catAdopter = new CatAdopter(testName, testMiddleName, testLastName, gender,
            LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")), phoneNumber,
            address, kindOfAnimalCat);

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(adopterController).isNotNull();
    }

    @Test
    public void whenAdopterIsSuccessfullySavedToDb() throws Exception {
        guest.setChatId(2L);

        when(dogAdopterRepository.findAll()).thenReturn(dogAdopterList);
        when(catAdopterRepository.findAll()).thenReturn(catAdopterList);

        when(guestRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(guest));

        when(catAdopterRepository.save(any(CatAdopter.class))).thenReturn(catAdopter);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/adopter-controller")
                        .param("name", testName)
                        .param("middleName", testMiddleName)
                        .param("lastName", testLastName)
                        .param("gender", gender)
                        .param("birthday", birthDate.toString())
                        .param("phoneNumber", phoneNumber)
                        .param("address", address)
                        .param("kindOfAnimal", kindOfAnimalCat))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAdopterIsAlreadyExists() throws Exception {
        catAdopterList.add(catAdopter);
        when(catAdopterRepository.findAll()).thenReturn(catAdopterList);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/adopter-controller")
                        .param("name", testName)
                        .param("middleName", testMiddleName)
                        .param("lastName", testLastName)
                        .param("gender", gender)
                        .param("birthday", birthDate.toString())
                        .param("phoneNumber", phoneNumber)
                        .param("address", address)
                        .param("kindOfAnimal", kindOfAnimalCat))
                .andExpect(status().isBadRequest());
    }

}
