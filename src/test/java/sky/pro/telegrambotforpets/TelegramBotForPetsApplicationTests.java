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
import sky.pro.telegrambotforpets.controllers.VolunteerController;
import sky.pro.telegrambotforpets.model.Guest;
import sky.pro.telegrambotforpets.model.Volunteer;
import sky.pro.telegrambotforpets.repositories.GuestRepository;
import sky.pro.telegrambotforpets.repositories.VolunteerRepository;
import sky.pro.telegrambotforpets.services.VolunteerServiceImpl;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VolunteerController.class)
class TelegramBotForPetsApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerRepository volunteerRepository;
    @MockBean
    private GuestRepository guestRepository;

    @SpyBean
    private VolunteerServiceImpl volunteerService;

    @InjectMocks
    private VolunteerController volunteerController;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(volunteerController).isNotNull();
    }

    private final String testName = "Test";
    private final String testPhoneNumber = "89997776611";


    @Test
    public void whenVolunteerIsSuccessfullySavedToDb() throws Exception {

        Guest guest = new Guest();

        Volunteer volunteer = new Volunteer();
        volunteer.setVolunteerPhoneNumber(testPhoneNumber);
        volunteer.setVolunteerName(testName);

        when(guestRepository.findByPhoneNumber(any(String.class))).thenReturn(java.util.Optional.of(guest));
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteers")
                        .param("name", testName)
                        .param("phoneNumber", testPhoneNumber))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSavingIsFailed() throws Exception {
        // сохранение волонтера провалится, если в БД гостей не найдется гость с таким номером телефона
        // по номеру тел достается chatId, который используется ботом для коммуникации с волонтером

        Volunteer volunteer = new Volunteer();
        volunteer.setVolunteerPhoneNumber(testPhoneNumber);
        volunteer.setVolunteerName(testName);

        when(guestRepository.findByPhoneNumber(any(String.class))).thenReturn(java.util.Optional.empty());
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteers")
                        .param("name", testName)
                        .param("phoneNumber", testPhoneNumber))
                .andExpect(status().isNotFound());

    }

}
