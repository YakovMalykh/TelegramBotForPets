package sky.pro.telegrambotforpets.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.telegrambotforpets.interfaces.ReportService;
import sky.pro.telegrambotforpets.model.Report;

import java.util.List;

@RestController
@RequestMapping("/report-controller")


public class ReportController {

    private final Logger logger = LoggerFactory.getLogger(AdopterController.class);
    private final ReportService reportService;

    public ReportController (ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Report>> getReports(
            @RequestParam("page") Integer pageNumber, @RequestParam("size") Integer pageSize
    ) {
        List<Report> reports = reportService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(reports);
    }



}
