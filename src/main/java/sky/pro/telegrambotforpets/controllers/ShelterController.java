package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.ShelterService;
import sky.pro.telegrambotforpets.model.Shelter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/shelter")
public class ShelterController {
    private final Logger logger = LoggerFactory.getLogger(ShelterController.class);
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @Operation(
            summary = "заносит новый приют в БД и сохраняет файлы в папку. В команде условились, что пока " +
                    "можно занести в Бд только 2 приюта(один для кошек, второй для собак, иначе нужно пересматривать " +
                    "логику обработки в телеграм-боте и в ShelterRepository.findShelterBySpecialization(String specialiZation)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "новый приют сохранен в БД"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "данные не сохранены, т.к. приют с таким именем уже существует в БД или " +
                                    "возникли проблемы с сохранением файлов, или передан недопустимый параметр " +
                                    "специализации"
                    )
            },
            description = "указанные поля не должны быть null или пустыми, иначе IllegalArgumentException"
    )
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveShelterToDB(
            @Parameter(description = "название приюта")
            @RequestParam String name,
            @Parameter(description = "адрес")
            @RequestParam String address,
            @Parameter(description = "график работы")
            @RequestParam String schedule,
            @Parameter(description = "специализация. Здесь используется Enum KindOfAnimal. " +
                    "Доступны 2 варианты: DOGS и CATS")
            @RequestParam KindOfAnimal kindOfAnimal,
            @Parameter(description = "общая информация о приюте")
            @RequestParam String description,
            @Parameter(description = "номер телефона охраны", example = "89991112233")
            @RequestParam String securityPhoneNumber,
            @Parameter(description = "схема проезда")
            @RequestParam MultipartFile howToGet,
            @Parameter(description = "правила безопасности в приюте")
            @RequestParam MultipartFile safetyRules
    ) {
        if (name == null||name.isEmpty() || address == null||address.isEmpty() || schedule == null||schedule.isEmpty()
                || kindOfAnimal == null || description == null||description.isEmpty() || securityPhoneNumber == null
                || securityPhoneNumber.isEmpty() || securityPhoneNumber.isBlank()
        ) {
            throw new IllegalArgumentException("переданы некорректные параметры");
        }
        Shelter shelter = new Shelter();
        shelter.setName(name);
        shelter.setAdress(address);
        shelter.setSchedule(schedule);
        shelter.setSpecialization(kindOfAnimal.name());
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

    @Operation(
            summary = "редактирует поля приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "иземенения внесены"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "IOException"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "приют с таким id не найден"
                    )
            },
            description = "все поля кроме id необязательные"
    )
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> editShelter(
            @Parameter(description = "Обязательное поле! id редактируемого приюта")
            @RequestParam Long id,
            @Parameter(description = "название приюта")
            @RequestParam(required = false) String name,
            @Parameter(description = "адрес")
            @RequestParam(required = false) String address,
            @Parameter(description = "график работы")
            @RequestParam(required = false) String schedule,
            @Parameter(description = "специализация. Здесь используется Enum Specialisations." +
                    " Доступны 2 варианты: DOGS и CATS")
            @RequestParam(required = false) KindOfAnimal kindOfAnimal,
            @Parameter(description = "общая информация о приюте")
            @RequestParam(required = false) String description,
            @Parameter(description = "номер телефона охраны")
            @RequestParam(required = false) String securityPhoneNumber,
            @Parameter(description = "схема проезда")
            @RequestParam(required = false) MultipartFile howToGet,
            @Parameter(description = "правила безопасности в приюте")
            @RequestParam(required = false) MultipartFile safetyRules
    ) {
        try {
            boolean done = shelterService.editShelter(id, name, address, schedule, kindOfAnimal, description, securityPhoneNumber,
                    howToGet, safetyRules);
            if (done) {
                logger.info("метод editShelter - изменения внесены");
                return ResponseEntity.ok().build();
            } else {
                logger.info("метод editShelter - приют с id " + id + " не найден");
                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("метод editShelter - IOException");
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(
            summary = "поиск приюта по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод вернул найденный приют",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "приют по данному id не найден"
                    )
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<Shelter> getShelterById(
            @Parameter(description = "id приюта в БД", example = "1")
            @PathVariable Long id) {
        Shelter shelter = shelterService.getShelter(id);
        if (shelter != null) {
            logger.info("метод getShelter - вернул прию с названием " + shelter.getName());
            return ResponseEntity.ok(shelter);
        } else {
            logger.info("метод getShelter - приют по " + id + " не найден");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "возвращает все приюты из БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "получен список приютов, содержащихся в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Shelter.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "в БД нет ниодного приюта"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Collection<Shelter>> getAllShelters() {
        List<Shelter> allShelters = shelterService.getAllShelters();
        if (allShelters.isEmpty()) {
            logger.info("метод getAllShelters вернул пустой список.");
            return ResponseEntity.notFound().build();
        } else {
            logger.info("метод getAllShelters вернул список приютов из БД");
            return ResponseEntity.ok(allShelters);
        }
    }

    @Operation(
            summary = "удаляет приют по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "приют удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "приют по id не найден"
                    )
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeShelter(
            @Parameter(description = "id приюта")
            @PathVariable Long id) {
        boolean done = shelterService.removeShelter(id);
        if (done) {
            logger.info("метод removeShelter - приют с id " + id + " удален");
            return ResponseEntity.ok().build();
        } else {
            logger.info("метод removeShelter - приют с id " + id + " не найден");
            return ResponseEntity.notFound().build();
        }
    }
}
