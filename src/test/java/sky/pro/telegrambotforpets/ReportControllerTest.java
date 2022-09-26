package sky.pro.telegrambotforpets;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sky.pro.telegrambotforpets.controllers.ReportController;
import sky.pro.telegrambotforpets.interfaces.AdoptionService;
import sky.pro.telegrambotforpets.model.Adoption;
import sky.pro.telegrambotforpets.model.Report;
import sky.pro.telegrambotforpets.repositories.*;
import sky.pro.telegrambotforpets.services.ReportServiceImpl;
import sky.pro.telegrambotforpets.services.SendInChatServiceImpl;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = ReportController.class)
public class ReportControllerTest {
    @Value("${path.to.reports.folder}")
    private String photoPath;

    @MockBean
    private ReportRepository reportRepository;
    @MockBean
    private AdoptionService adoptionService;
    @MockBean
    private SendInChatServiceImpl sendInChatService;
    @MockBean
    private AdoptionRepository adoptionRepository;
    @MockBean
    private CatAdopterRepository catAdopterRepository;
    @MockBean
    private DogAdopterRepository dogAdopterRepository;
    @MockBean
    private AdopterRepository adopterRepository;
    // @MockBean
    @SpyBean
    private ReportServiceImpl reportService;
    @InjectMocks
    private ReportController reportController;
    @Autowired
    private MockMvc mockMvc;
    private Long adoptionId = 1L;
    private Document photos;
    private Long chatId = 1l;
    private String behaivor = "Behaivor";
    private String ration = "Ration";
    private String feeling = "Feeling";
    private Report report = new Report();
    private JSONObject reportObject = new JSONObject();
    private Adoption adoption;
    @MockBean
    private TelegramBot bot;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2022, 9, 24);
        Path path = Path.of("reports", String.valueOf(1l), date.toString());
        adoption = new Adoption("CATS", 1l, 1l, date);
        adoption.setId(1L);
        reportObject.put("id", 1L);
        reportObject.put("ration", ration);
        reportObject.put("behaivor", behaivor);
        reportObject.put("feeling", feeling);
        report.setId(1L);
        report.setRation(ration);
        report.setBehaivor(behaivor);
        report.setFeeling(feeling);
        when(reportRepository.findReportByAdoptionId(any(Long.class))).thenReturn(List.of(report));
        when(reportRepository.findReportByDateAndAdoptionId(Mockito.any(), Mockito.any(Long.class))).thenReturn(Optional.of(report));
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(reportController).isNotNull();
        Assertions.assertThat(reportService.findAll(Mockito.any(Integer.class),Mockito.any(Integer.class))).isNotNull();
        List<Report> reportNew = reportRepository.findReportByAdoptionId(adoptionId);
        List<Report> reportList = List.of(report);
        assertEquals(reportList, reportNew);
    }

    @Test
    public void getReportsByAdoptionIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report-controller/adoptionId")
                        .param("adoptionId", adoption.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getReportsByDateAndAdoptionIdTest() throws Exception {
        LocalDate date = LocalDate.of(2022, 9, 22);
        Optional<Report> reportNew = reportRepository.findReportByDateAndAdoptionId(date, adoptionId);
        System.out.println(reportNew);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report-controller/adoptionId")
                        .param("date", "22.09.2022")
                        .param("adoptionId", adoption.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
