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
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.PetService;
import sky.pro.telegrambotforpets.model.Pet;

import javax.xml.crypto.Data;

import java.util.Collection;

import static sky.pro.telegrambotforpets.constants.KindOfAnimal.*;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final Logger logger = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(
            summary = "сохранение нового питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "питомец сохранен в БД"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "выбрана неверный приют для данного вида животного, такой питомец уже есть в БД" +
                                    "или приюта с таким ID не существует"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> savePetToDB(
            @Parameter(description = "кличка")
            @RequestParam String name,
            @Parameter(description = "требуется строка вида dd.MM.yyyy")
            @RequestParam String birthDay,
            @Parameter(description = "пол")
            @RequestParam Gender gender,
            @Parameter(description = "идентификатор породы")
            @RequestParam Long breedId,
            @Parameter(description = "стерилизован?")
            @RequestParam Boolean sterilized,
            @Parameter(description = "Требуется особвый уход(инвалид)?")
            @RequestParam Boolean invalid,
            @Parameter(description = "Вид животного. На основе этого параметра определяется, в какую БД сохранится" +
                    " питомец")
            @RequestParam KindOfAnimal kindOfAnimal,
            @Parameter(description = "ID приюта, в котором будет содержаться животное")
            @RequestParam Long shelterId) {
        boolean done = petService.savePetToDB(name, birthDay, gender, breedId, sterilized,
                invalid, kindOfAnimal, shelterId);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "редактирует питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "указанные поля отредактированы"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "питомец с таким Id не найден"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<Void> editPet(
            @RequestParam Long petId,
            @Parameter(description = "вид животного")
            @RequestParam KindOfAnimal kindOfAnimal,
            @Parameter(description = "кличка")
            @RequestParam(required = false) String name,
            @Parameter(description = "дата рождения. требуется строка вида dd.MM.yyyy")
            @RequestParam(required = false) String birthDay,
            @Parameter(description = "пол")
            @RequestParam(required = false) Gender gender,
            @Parameter(description = "id породы")
            @RequestParam(required = false) Long breedId,
            @Parameter(description = "стерилизован?")
            @RequestParam(required = false) Boolean sterilized,
            @Parameter(description = "инвалид?")
            @RequestParam(required = false) Boolean invalid,
            @Parameter(description = "ID приюта, в котором содержится животное")
            @RequestParam(required = false) Long shelterId
    ) {
        boolean done = petService.editPet(petId, kindOfAnimal, name, birthDay, gender, breedId, sterilized,
                invalid, shelterId);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(
            summary = "Поиск по ID. По KindOfAnimal (вид животного)" +
                    "определяет, в какой БД искать питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "вернет питомца (кошку или собаку)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "питомец по такому ID не найден"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPet(
            @PathVariable Long id,
            @RequestParam KindOfAnimal kindOfAnimal
    ) {
        Pet pet = petService.getPetById(id, kindOfAnimal);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(pet);
        }
    }

    @Operation(
            summary = "Поиск по имени. Ищет полное совпадении игнорирую регистр. По KindOfAnimal (вид животного)" +
                    "определяет, в какой БД искать питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "вернет питомца (кошку или собаку)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "питомец с такой кличкой не найден"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Pet> findPetByName(
            @Parameter(description = "кличка")
            @RequestParam String name,
            @Parameter(description = "вид животного")
            @RequestParam KindOfAnimal kindOfAnimal
    ) {
        Pet pet = petService.getPetByName(name, kindOfAnimal);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(pet);
        }

    }

    @Operation(
            summary = "получение списка питомцев. По KindOfAnimal (вид животного) определяет, " +
                    "в какой БД искать питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "вернет список кошек или собак",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Pet.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "список питомцев пуст"
                    )
            }
    )
    @GetMapping("/get-list-pets")
    public ResponseEntity<Collection<? extends Pet>> getListOfAllPets(
            @Parameter(description = "вид животного")
            KindOfAnimal kindOfAnimal) {
        Collection<? extends Pet> listPets = petService.getListOfAllPets(kindOfAnimal);
        if (listPets.isEmpty()) {
            logger.info("метод getListOfAllPets - вернул пустой список питомцев");
            return ResponseEntity.notFound().build();
        } else {
            logger.info("метод getListOfAllPets - вернул список питомцев");
            return ResponseEntity.ok(listPets);
        }
    }

    @Operation(
            summary = "удаление питомца по id.По KindOfAnimal (вид животного) определяет, " +
                    "из какой БД удалить питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "питомец успешно удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "питомца с таким id нет в БД"
                    )
            }
    )
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> removePet(
            @Parameter(description = "вид животоного")
            @RequestParam KindOfAnimal kindOfAnimal,
            @PathVariable Long id
    ) {
        boolean done = petService.removePet(kindOfAnimal, id);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
