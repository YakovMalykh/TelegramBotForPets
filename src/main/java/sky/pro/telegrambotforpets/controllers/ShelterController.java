package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.interfaces.ShelterService;
import sky.pro.telegrambotforpets.model.Shelter;

import java.io.IOException;

@RestController
@RequestMapping("/shelter")
public class ShelterController {
    private final Logger logger = LoggerFactory.getLogger(ShelterController.class);
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveShelterToDB(
            @RequestParam(defaultValue = "Приют для собак") String name,
            @RequestParam(defaultValue = "Москва") String address,
            @RequestParam(defaultValue = "с 10:00 до 21:00") String schedule,
            @RequestParam(defaultValue = "dogs") String specialization,
            @RequestParam(defaultValue = "Приют работает с 2019 г.") String description,
            @RequestParam(defaultValue = "+7(900) 222-11-33") String securityPhoneNumber,
            @RequestParam(value = "howToGet") MultipartFile howToGet,
            @RequestParam(value = "safetyRules") MultipartFile safetyRules
            ) {
        Shelter shelter = new Shelter();
        shelter.setName(name);
        shelter.setAdress(address);
        shelter.setSchedule(schedule);
        shelter.setSpecialization(specialization);
        shelter.setDescription(description);
        shelter.setSecurityPhoneNumber(securityPhoneNumber);

        try {
            boolean done = shelterService.saveShelterToDB(shelter, howToGet, safetyRules);
            if (done) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
