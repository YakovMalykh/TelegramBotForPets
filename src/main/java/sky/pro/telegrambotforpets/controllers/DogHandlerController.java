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
import sky.pro.telegrambotforpets.interfaces.DogHandlerService;
import sky.pro.telegrambotforpets.model.DogHandler;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/dog-handlers")
public class DogHandlerController {
    private Logger logger = LoggerFactory.getLogger(DogHandlerController.class);

    private final DogHandlerService dogHandlerService;

    public DogHandlerController(DogHandlerService dogHandlerService) {
        this.dogHandlerService = dogHandlerService;
    }

    @Operation(
            summary = "сохранение нового кинолога в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "кинолог сохранен"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "передан null в качестве параметра или такой кинолог уже" +
                                    " есть в БД"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "сохраняемый кинолог, прописывать id кинолога не требуеся, т.к. генерируется автоматически",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogHandler.class)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Void> saveDogHandlerToDB(
            @RequestBody DogHandler dogHandler) {
        if (dogHandler == null) {
            logger.info("передан null");
            return ResponseEntity.badRequest().build();
        } else {
            boolean dogHandlerSaveToDB = dogHandlerService.saveDogHandlerToDB(dogHandler);
            if (dogHandlerSaveToDB) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(
            summary = "изменение существующего кинолога в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "кинолог сохранен"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "передан null в качестве параметра"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "кинолог с таким id не найден"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "изменяемый кинолог, обязательно указать id, чтобы найти " +
                            "существующего в БД кинолого, иначе вернется NotFound",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogHandler.class)
                    )
            )
    )
    @PutMapping
    public ResponseEntity<Void> editDogHandler(@RequestBody DogHandler dogHandler) {
        if (dogHandler == null) {
            logger.info("передан null");
            return ResponseEntity.badRequest().build();
        } else {
            boolean edited = dogHandlerService.editDogHandler(dogHandler);
            if (edited) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @Operation(
            summary = "получение кинолога по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод вернул найденного по id кинолога",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "кинолог по этому id не найден"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "в параметры передан null"
                    )
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<DogHandler> getDogHandlerById(
            @Parameter(description = "id кинолога в БД", example = "1")
            @PathVariable Long id) {
        if (id != null) {
            DogHandler dogHandler = dogHandlerService.getDogHandler(id);
            if (dogHandler == null) {
                logger.info("метод getDogHandler - кинолог по этому id не найден");
                return ResponseEntity.notFound().build();
            } else {
                logger.info("метод getDogHandler вернул найденного по id кинолога");
                return ResponseEntity.ok(dogHandler);
            }

        } else {
            logger.info("в параметры передан null");
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "получение списка из всех кинологов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "метод вернул список из всех кинологов из БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogHandler.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "БД не содержит ниодного кинолога"
                    )
            }
    )

    @GetMapping
    public ResponseEntity<Collection<DogHandler>> getAllDogHandlers() {
        List<DogHandler> allDogHandlers = dogHandlerService.getAllDogHandlers();
        if (allDogHandlers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(allDogHandlers);
        }
    }

    @Operation(
            summary = "удаление кинолога из БД по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "кинолог удален из БД"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "в параметры передан null"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "кинолог по этому id не найден"
                    )
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeDogHandlerById(
            @Parameter(description = "id кинолога в БД", example = "1")
            @PathVariable Long id
    ) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        } else {
            boolean removed = dogHandlerService.removeDogHandler(id);
            if (removed) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }


}
