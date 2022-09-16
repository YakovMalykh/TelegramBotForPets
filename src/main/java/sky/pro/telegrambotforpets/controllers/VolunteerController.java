package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.telegrambotforpets.interfaces.VolunteerService;

@RestController
@RequestMapping("/volunteers")
public class VolunteerController {

    private final Logger logger = LoggerFactory.getLogger(VolunteerController.class);
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }
    @Operation(
            summary = "сохраняет волонтера в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "успешно сохранен"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "не смог получить chatId из базы гостей, требуется сначала в качестве " +
                                    "гостя создать чат с ботом и поделиться с ним контактом"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> saveVolunteerToDB(
            @Parameter(description = "имя")
            @RequestParam String name,
            @Parameter(description = "номер телефона. сохранит в виде 89997770022. По нему будет искать в БД гостя с " +
                    "таким номером тел и достанет у него chatId")
            @RequestParam String phoneNumber) {
        boolean done = volunteerService.saveVolunteerToDB(name, phoneNumber);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
