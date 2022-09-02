package sky.pro.telegrambotforpets.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveDocumentToDb(
            @RequestParam String description,
            @RequestParam MultipartFile file) {
        try {
            docForPrepService.saveDocumentToDB(description, file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> editDocument(
            @RequestParam String description,
            @RequestParam MultipartFile file) {
        try {
            docForPrepService.editDocuments(description, file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * метод вычитывает файл из его папки
     * @param documentId
     * @param response
     * @throws IOException
     * @see sky.pro.telegrambotforpets.services.DocumentsForPreparationServiceImpl#getDocument(Integer)
     */
    @GetMapping(value = "/{documentId}")
    public void getDocument(@PathVariable Integer documentId,
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
            }
        }

    }

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
            @RequestParam(required = false) String partDescription) {
        if (partDescription != null) {
            return docForPrepService.getDocuments(partDescription);
        } else {
            return docForPrepService.getAllDocuments();
        }
    }

    @DeleteMapping(value = "/{documentId}")
    public ResponseEntity<Void> removeDocument(@PathVariable Integer documentId) throws IOException{
          return docForPrepService.removeDocument(documentId);
    }


}
