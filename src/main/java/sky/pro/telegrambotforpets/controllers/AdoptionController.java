package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sky.pro.telegrambotforpets.constants.AdoptionsResult;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.AdoptionService;
import sky.pro.telegrambotforpets.model.Adoption;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    private final Logger logger = LoggerFactory.getLogger(AdoptionController.class);
    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @Operation(
            summary = "создает новую запись об усыновлении в БД и присваивает питомцу усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "все прошло успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "по ID не найден питомец или усыновитель"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> saveAdoptionToDb(
            @RequestParam KindOfAnimal kindOfAnimal,
            @RequestParam Long petId,
            @RequestParam Long adopterId
    ) {
        boolean done = adoptionService.saveAdoptionToDB(kindOfAnimal, petId, adopterId);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "волонтер устанавливает результат по итогам 30 дней",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "успешно"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "записи об усныновлении с таким ID не найдено"
                    )
            }
    )
    @PutMapping("/{adoptionId}")
    public ResponseEntity<Void> setAdoptionsResult(
            @PathVariable Long adoptionId,
            @RequestParam AdoptionsResult adoptionsResult) {

        boolean done = adoptionService.setAdoptionsResult(adoptionId, adoptionsResult);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "поиск запис об усыновлении по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "успешно"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "записи об усныновлении с таким ID не найдено"
                    )
            }
    )
    @GetMapping("/{adoptionId}")
    public ResponseEntity<Adoption> getAdoptionById(@PathVariable Long adoptionId) {
        Optional<Adoption> adoptionById = adoptionService.getAdoptionById(adoptionId);
        if (adoptionById.isPresent()) {
            logger.info("метод getAdoptionById - вернул запись об усыновлении из БД");
            return ResponseEntity.ok(adoptionById.get());
        } else {
            logger.info("метод getAdoptionById - записи об усыновлении с таким ID не найдено в БД");
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(
            summary = "возвращается список всех записей об усыновлении из БД"
    )
    @GetMapping
    public ResponseEntity<Collection<Adoption>> getAllAdoptions() {
        List<Adoption> allAdoptions = adoptionService.getAllAdoptions();
        return ResponseEntity.ok(allAdoptions);
    }
    @Operation(
            summary = "возвращает список записей об усыновлении, у которых испытательный срок заканчивается сегодня" +
                    "в том числе после продления"
    )
    @GetMapping("/trial-period-ends-today")
    public ResponseEntity<Collection<Adoption>> trialPeriodEndsToday() {
        List<Adoption> adoptions = adoptionService.trialPeriodEndsToday();
        return ResponseEntity.ok(adoptions);
    }

}
