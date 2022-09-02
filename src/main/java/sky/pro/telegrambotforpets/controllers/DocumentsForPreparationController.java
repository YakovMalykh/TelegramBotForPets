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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public void getDocument(@PathVariable Long docId,
                            HttpServletResponse response) throws IOException {
        Integer documentId = docId.intValue();

        DocumentsForPreparation document = docForPrepService.getDocument(documentId).getBody();
        if (document != null) {
            Path path = Path.of(document.getFilePath());

            try (
                    InputStream is = Files.newInputStream(path);
                    OutputStream os = response.getOutputStream();
            ) {
                response.setStatus(200);
                response.setContentType(null);
                response.setContentLength();
                is.transferTo(os);
            }
        }

    }

    @GetMapping
    public ResponseEntity<Collection<DocumentsForPreparation>> getListOfDocuments(
            @RequestParam(required = false) String partDescription) {
        if (partDescription != null) {
            return docForPrepService.getDocuments(partDescription);
        } else {
            return docForPrepService.getAllDocuments();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDocument(@PathVariable Integer documentId) {
        return docForPrepService.removeDocument(documentId);
    }


}
