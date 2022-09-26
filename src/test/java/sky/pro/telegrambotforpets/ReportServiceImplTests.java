package sky.pro.telegrambotforpets;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import sky.pro.telegrambotforpets.interfaces.AdoptionService;
import sky.pro.telegrambotforpets.model.Adoption;
import sky.pro.telegrambotforpets.model.Report;
import sky.pro.telegrambotforpets.repositories.ReportRepository;
import sky.pro.telegrambotforpets.services.ReportServiceImpl;
import sky.pro.telegrambotforpets.services.SendInChatServiceImpl;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReportServiceImplTests {
    @Value("${path.to.reports.folder}")
    private String photoPath;

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private AdoptionService adoptionService;
    @Mock
    private SendInChatServiceImpl sendInChatService;

    private Long adoptionId = 1L;
    private Document photos;
    private Long chatId = 1l;
    @Mock
    private TelegramBot bot;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2022, 9, 24);
        Path path = Path.of("reports", String.valueOf(1l), date.toString());
        Adoption adoption = new Adoption("CATS", 1l, 1l, date);
        adoption.setId(1L);
        Report report = new Report();
        report.setId(1L);
        report.setRation("Ration");
        report.setDate(date);
        report.setBehaivor("Behaivor");
        report.setFeeling("Feeling");
        report.setPhotoPath(String.valueOf(path));
        report.setAdoption(adoption);

        when(reportRepository.findReportByDateAndAdoptionId(any(LocalDate.class), any(Long.class))).thenReturn(Optional.of(report));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForTest")
    public void saveReportFildToDBTest(Report report, LocalDate date, Long adoptionId) {
        Optional<Report> reportNew = reportRepository.findReportByDateAndAdoptionId(date, adoptionId);
        assertEquals(report, reportNew.get());
        assertTrue(reportNew.isPresent()
                && report.equals(reportNew.get()));

        verify(reportRepository, times(1)).findReportByDateAndAdoptionId(date, adoptionId);
    }

    public static Stream<Arguments> provideParamsForTest() {
        LocalDate date = LocalDate.of(2022, 9, 24);
        Path path = Path.of("reports", String.valueOf(1l), date.toString());
        Adoption adoption = new Adoption("CATS", 1l, 1l, date);
        adoption.setId(1L);
        Report report = new Report();
        report.setId(1l);
        report.setRation("Ration");
        report.setDate(date);
        report.setBehaivor("Behaivor");
        report.setFeeling("Feeling");
        report.setPhotoPath(String.valueOf(path));
        report.setAdoption(adoption);
        return Stream.of(
                Arguments.of(report, date, 1l)
        );
    }

}
