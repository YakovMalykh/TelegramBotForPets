package sky.pro.telegrambotforpets.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.model.DocumentsForPreparation;
import sky.pro.telegrambotforpets.repositories.DocumentsForPreparationRepository;

import java.io.IOException;
import java.util.Collection;

public interface DocumentsForPreparationService {
    /**
     * @param description
     * @param file
     * @return
     * @throws IOException
     */
    ResponseEntity<Void> saveDocumentToDB(String description, MultipartFile file)throws IOException;
    /**
     * equals проверяет только поле Id, поэтому при пересмотре логики equals
     * пересмотреть логику проверки в этом методе
     *
     * @param  description,  file
     * @return ResponseEntity<Void>
     * @throws org.springframework.web.client.HttpClientErrorException.BadRequest
     * @throws org.springframework.web.client.HttpClientErrorException.NotFound
     * @see DocumentsForPreparation#equals(Object)
     */
    ResponseEntity<Void> editDocuments(String description, MultipartFile file)throws IOException;

    ResponseEntity<DocumentsForPreparation> getDocument(Integer documentId);

    /**
     * метод ищет по совпадению части описания (description)
     * @param partDescription
     * @return List<'DocumentsForPreparation'>
     * @see DocumentsForPreparationRepository#findByDescriptionContainingIgnoreCase(String)
     * @throws HttpClientErrorException.BadRequest,HttpClientErrorException.NotFound
     */
    ResponseEntity<Collection<DocumentsForPreparation>> getDocuments(String partDescription);

    ResponseEntity<Collection<DocumentsForPreparation>> getAllDocuments();

    /**
     * @param documentId
     * @return
     * @throws HttpClientErrorException.BadRequest,HttpClientErrorException.NotFound
     */
    ResponseEntity<Void> removeDocument(Integer documentId);



}
