package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import sky.pro.telegrambotforpets.constants.Descriptions;
import sky.pro.telegrambotforpets.interfaces.DocumentsForPreparationService;
import sky.pro.telegrambotforpets.model.DocumentsForPreparation;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/documents-preparation")
public class DocumentsForPreparationController {

    private final DocumentsForPreparationService docForPrepService;

    public DocumentsForPreparationController(DocumentsForPreparationService docForPrepService) {
        this.docForPrepService = docForPrepService;
    }

    @Operation(
            summary = "Сохранение документа в БД и файла в папку",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "документ сохранен"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "если документа по указанному пути не существует"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "если передано недопустимое описание документа, т.е. " +
                                    "такого элемента нет в enum Descriptions или документ с таким" +
                                    "описанием уже существует"
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveDocumentToDb(
            @Parameter(description = "Описание документа. Доступны следующие варианты: " +
                    "DOG_DAITING_RULES, LIST_DOCUMENTS_FOR_ADOPTING, TRANSPORTATION_ADVICE, " +
                    "PREPARING_HOUSE_FOR_A_PUPPY, PREPARING_HOUSE_FOR_AN_ADULT_DOG, " +
                    "PREPARING_HOUSE_FOR_A_DISABLED_DOG, DOGHANDLER_ADVICIES, REASONS_FOR_REFUSAL",
                    example = "PREPARING_HOUSE_FOR_A_PUPPY")
            @RequestParam(name = "здесь используется enum Descriptions") Descriptions description,
            @RequestParam(name = "загружаем файл") MultipartFile file) {
        try {
            boolean doesDocSave = docForPrepService.saveDocumentToDB(description.name(), file);
            if(doesDocSave){
                return ResponseEntity.ok().build();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @Operation(
            summary = "редактирование существующего документа в БД и замена файла в папке на новый",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "документ успешно изменен, новый файл сохранен"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "если документа по указанному пути не существует"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "если передано недопустимое описание документа, т.е. " +
                                    "такого элемента нет в enum Descriptions"
                    )
            }
    )
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> editDocument(
            @Parameter(description = "нужно передать точное описание документа, чтобы по нему был " +
                    "найден документ в БД. Доступны следующие варианты: " +
                    "DOG_DAITING_RULES, LIST_DOCUMENTS_FOR_ADOPTING, TRANSPORTATION_ADVICE, " +
                    "PREPARING_HOUSE_FOR_A_PUPPY, PREPARING_HOUSE_FOR_AN_ADULT_DOG, " +
                    "PREPARING_HOUSE_FOR_A_DISABLED_DOG, DOGHANDLER_ADVICIES, REASONS_FOR_REFUSAL",
                    example = "PREPARING_HOUSE_FOR_A_PUPPY")
            @RequestParam(name = "здесь используется enum Descriptions") Descriptions description,
            @RequestParam(name = "загружаем файл") MultipartFile file) {
        try {
            docForPrepService.editDocuments(description.name(), file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "поиск документа по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "найденный документ",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Если докуента по переданному ID нет в БД"
                    )
            }
    )
    /**
     * метод вычитывает файл из его папки
     *
     * @param documentId
     * @param response
     * @throws IOException
     * @see sky.pro.telegrambotforpets.services.DocumentsForPreparationServiceImpl#getDocument(Integer)
     */
    @GetMapping(value = "/{documentId}")
    public ResponseEntity<Void> getDocument(
            @Parameter(description = "ID документа в БД", example = "4")
            @PathVariable Integer documentId,
            HttpServletResponse response) throws IOException {

        DocumentsForPreparation document = docForPrepService.getDocument(documentId).getBody();
        if (document != null) {
            Path path = Path.of(document.getFilePath());

            try (
                    InputStream is = Files.newInputStream(path);
                    OutputStream os = response.getOutputStream();
            ) {
                response.setStatus(200);
                response.setContentType(document.getMediaType());
                response.setContentLength(document.getFileSize().intValue());
                is.transferTo(os);
                return ResponseEntity.ok().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(
            summary = "Вернет список из всех документов хранящихся БД или из документов найденных" +
                    " по части описания",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список найденных документов",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = DocumentsForPreparation.class)
                                    )
                            )
                    )
            }
    )
    /**
     * возвращает список документов в формате json. Если вызвать без параметра, то выдаст список всех
     * документов, содержащихся в базе. При передаче параметра (часть строки) поиск идет по частичному
     * совпадению в поле description
     *
     * @param partDescription
     * @return List of DocumentsForPreparation
     */
    @GetMapping
    public ResponseEntity<Collection<DocumentsForPreparation>> getListOfDocuments(
            @Parameter(description = "параметр необязательный, вводится часть описания документа. " +
                    "Если параметр непустой, то вызывается метод поиска по части описания игнорируя регистр," +
                    " если же параметр пустой, то вызовется метод получения всех документов из БД",
                    example = "dog")
            @RequestParam(required = false, name = "часть описания документа") String partDescription) {
        if (partDescription != null) {
            return docForPrepService.getDocuments(partDescription);
        } else {
            return docForPrepService.getAllDocuments();
        }
    }


    @Operation(
            summary = "удаление документа из БД и файла из папки по переданному ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Документ удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Документ с заданным ID не найден в БД"
                    )
            }
    )
    @DeleteMapping(value = "/{documentId}")
    public ResponseEntity<Void> removeDocument(
            @Parameter(description = "ID документа в БД")
            @PathVariable Integer documentId) throws IOException {
        return docForPrepService.removeDocument(documentId);
    }


}


