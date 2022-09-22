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
import sky.pro.telegrambotforpets.interfaces.AdopterService;
import sky.pro.telegrambotforpets.model.Adopter;

import java.util.Collection;

@RestController
@RequestMapping("/adopter-controller")
public class AdopterController {

    private final Logger logger = LoggerFactory.getLogger(AdopterController.class);
    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @Operation(
            summary = "сохраняет новго усыновителя в соответсвующую БД, в зависимости от вида животного",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "усыновитель успешно сохранен в Бд"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "такой усыновитель уже есть в БД"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> saveAdopterToDB(
            @Parameter(description = "имя")
            @RequestParam String name,
            @Parameter(description = "отчество")
            @RequestParam String middleName,
            @Parameter(description = "фамилия")
            @RequestParam String lastName,
            @Parameter(description = "пол")
            @RequestParam Gender gender,
            @Parameter(description = "требуется строка вида dd.MM.yyyy")
            @RequestParam String birthday,
            @Parameter(description = "номер телефона. сохранит в виде 89997770022")
            @RequestParam String phoneNumber,
            @Parameter(description = "адрес")
            @RequestParam String address,
            @Parameter(description = "вид животного. на основании этого параметра выбирается БД, " +
                    "в которую сохранится усыновитель")
            @RequestParam KindOfAnimal kindOfAnimal) {
        boolean done = adopterService.saveAdopterToDB(name, middleName, lastName, gender, birthday, phoneNumber,
                address, kindOfAnimal);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "редкатирует поля усыновителя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "поля успешно сохранены"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Усыновитель с таким id не найден"
                    )
            }
    )
    @PutMapping("/{adopterId}")
    public ResponseEntity<Void> editAdopter(
            @Parameter(description = "id усыновителя")
            @PathVariable Long adopterId,
            @Parameter(description = "вид животного. на основании этого параметра выбирается БД, где искать усыновителя")
            @RequestParam KindOfAnimal kindOfAnimal,
            @Parameter(description = "имя")
            @RequestParam(required = false) String name,
            @Parameter(description = "отчество")
            @RequestParam(required = false) String middleName,
            @Parameter(description = "фамилия")
            @RequestParam(required = false) String lastName,
            @Parameter(description = "пол")
            @RequestParam(required = false) Gender gender,
            @Parameter(description = "требуется строка вида dd.MM.yyyy")
            @RequestParam(required = false) String birthday,
            @Parameter(description = "номер телефона. сохранит в виде 89997770022")
            @RequestParam(required = false) String phoneNumber,
            @Parameter(description = "адрес")
            @RequestParam(required = false) String address
    ) {
        boolean done = adopterService.editAdopter(adopterId, kindOfAnimal, name, middleName, lastName, gender,
                birthday, phoneNumber, address);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "посик усыновителя по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "получен усыновитель по переданному Id",
                            content = @Content(schema = @Schema(implementation = Adopter.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "усыновитель не найден"
                    )
            }
    )
    @GetMapping("/{adopterId}")
    public ResponseEntity<Adopter> getAdopterById(
            @PathVariable Long adopterId,
            @Parameter(description = "вид животного, по нему определяется в какой БД нужно искать усыновителя")
            @RequestParam KindOfAnimal kindOfAnimal) {
        Adopter adopter = adopterService.getAdopterById(adopterId, kindOfAnimal);
        if (adopter != null) {
            return ResponseEntity.ok(adopter);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(
            summary = "поиск усыновителя по номеру телефона",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "получен усыновитель",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adopter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "усыновитель по номеру телефона не найден"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Adopter> getAdopterByPhoneNumber(
            @Parameter(description = "вид животного, по нему определяется в какой БД нужно искать усыновителя")
            @RequestParam KindOfAnimal kindOfAnimal,
            @Parameter(description = "требуется номер телефона в виде 89997770022")
            @RequestParam String phoneNumber
    ) {
        Adopter adopter = adopterService.getAdopterByPhoneNumber(kindOfAnimal, phoneNumber);
        if (adopter != null) {
            logger.info("метод getAdopterByPhoneNumber - вернул усыновителя с именем " + adopter.getName());
            return ResponseEntity.ok(adopter);
        } else {
            logger.info("метод getAdopterByPhoneNumber - усыновителя с таким номером телефона нет в БД");
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(
            summary = "получение списка усыновителей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "вернулся список усыновителей",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Adopter.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "усыновителей в базе нет"
                    )
            }
    )
    @GetMapping("/get-list-adopters")
    public ResponseEntity<Collection<? extends Adopter>> getListAdopters(
            @Parameter(description = "вид животного, по нему определяется из какой БД нужно вывести список")
            @RequestParam KindOfAnimal kindOfAnimal
    ){
        Collection<? extends Adopter> listAdopters = adopterService.getListAdopters(kindOfAnimal);
        if (listAdopters.isEmpty()) {
            logger.info("метод getListAdopters вернул пустой спиоск усыновителей");
            return ResponseEntity.notFound().build();
        } else {
            logger.info("метод getListAdopters вернул спиоск усыновителей");
            return ResponseEntity.ok(listAdopters);
        }
    }
    @Operation(
            summary = "удаляет усыновителя по id",
            responses =  {
                    @ApiResponse(
                            responseCode = "200",
                            description = "усыновитель успешно удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Усыновитель по указанному id не найден"
                    )
            }
    )
    @DeleteMapping("/{adopterId}")
    public ResponseEntity<Void> removeAdopter(
            @Parameter(description = "вид животного, по нему определяется из какой БД нужно удалить усыновителя")
            @RequestParam KindOfAnimal kindOfAnimal,
            @PathVariable Long adopterId) {
        boolean done = adopterService.removeAdopter(kindOfAnimal, adopterId);
        if (done) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
