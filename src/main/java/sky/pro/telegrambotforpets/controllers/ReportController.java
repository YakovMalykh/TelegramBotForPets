package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.telegrambotforpets.interfaces.ReportService;
import sky.pro.telegrambotforpets.model.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/report-controller")


public class ReportController {

    private final Logger logger = LoggerFactory.getLogger(AdopterController.class);
    private final ReportService reportService;

    public ReportController (ReportService reportService) {
        this.reportService = reportService;
    }
    @Operation(
            summary = "вывод всех отчетов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод вернет все отчеты из базы данных, если отчетов нет, вернет пустой список",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<Report>> getReports(
           @RequestParam("page") Integer pageNumber, @RequestParam("size") Integer pageSize
    ) {
        List<Report> reports = reportService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(reports);
    }
    @Operation(
            summary = "вывод всех отчетов по заданному усыновлению.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод вернет все отчеты для данного усыновления, если отчетов нет, вернет пустой список",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping("/adoptionId")
    public ResponseEntity<List<Report>> getReportsByAdoptionId(
            @RequestParam Long adoptionId
    ) {
        List <Report> reports=new ArrayList<>();
        reports = reportService.findByAdoptionId(adoptionId);
        return ResponseEntity.ok(reports);
    }
    @Operation(
            summary = "вывод всех отчетов по заданному усыновлению и дате.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод вернет все отчеты для данного усыновления по дате, если отчетов нет, вернет пустой список",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping("/dateAndadoptionId")
    public ResponseEntity<Optional<Report>> getReportsByDateAndAdoptionId(
            @RequestParam LocalDate date, @RequestParam Long adoptionId
    ) {
        Optional<Report> reports = reportService.findByDateAndAdoptionId(date, adoptionId);
        return ResponseEntity.ok(reports);

    }

}
