package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.interfaces.DocumentsForPreparationService;
import sky.pro.telegrambotforpets.model.DocumentsForPreparation;
import sky.pro.telegrambotforpets.repositories.DocumentsForPreparationRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
@Transactional
public class DocumentsForPreparationServiceImpl implements DocumentsForPreparationService {

    private Logger logger = LoggerFactory.getLogger(DocumentsForPreparationServiceImpl.class);

    @Value("${path.to.documents-for-preparation.folder}")
    private String documentsPrepFolder;
    private final DocumentsForPreparationRepository documentsForPreparationRepository;

    public DocumentsForPreparationServiceImpl(DocumentsForPreparationRepository documentsForPreparationRepository) {
        this.documentsForPreparationRepository = documentsForPreparationRepository;
    }

    /**
     * создается папка по указанному пути, если не создана, считывается туда переданный файл и сохраняется
     * путь к нему в сущности @DocumentsForPreparation
     *
     * @param description
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<Void> saveDocumentToDB(String description, MultipartFile file) throws IOException {
        if (description != null && file != null) {

            Path filePath = Path.of(documentsPrepFolder, description + "." + getExtention(file));

            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }
            DocumentsForPreparation document = new DocumentsForPreparation();
            document.setDescription(description);
            document.setFilePath(filePath.toString());
            document.setFileSize(file.getSize());
            document.setMediaType(file.getContentType());

            documentsForPreparationRepository.save(document);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * получаю расширение файла переданного пользователем
     *
     * @param file
     * @return
     */
    private String getExtention(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /**
     * по описанию нахожу документ, если он есть, достаю из него путь к файлу, удаляю файл по этому пути,
     * создаю новый путь на основании нового файла и копирую новый файл в директорию по новому пути,
     * устанавливаю в документ новый путь к файлу, записываю обновленный документ в БД
     *
     * @param description, file
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<Void> editDocuments(String description, MultipartFile file) throws IOException {
        if (description == null && file == null) {
            return ResponseEntity.badRequest().build();
        } else {
            DocumentsForPreparation document =
                    documentsForPreparationRepository.findFirstByDescriptionIgnoreCase(description);
            if (document!=null) {

                /**
                 * получаю путь к файлу из этогот документа и удаляю его за ненадобностью
                 */
                String filePathInString = document.getFilePath();
                Path filePath = Path.of(filePathInString);
                Files.deleteIfExists(filePath);

                /**
                 * создаю новый путь
                 */
                Path newFilePath = Path.of(documentsPrepFolder, description + "." + getExtention(file));
                /**
                 * кладу нолвый файл по новому пути
                 */
                try(
                        InputStream is = file.getInputStream();
                        OutputStream os = Files.newOutputStream(newFilePath, CREATE_NEW);
                        BufferedInputStream bis = new BufferedInputStream(is,1024);
                        BufferedOutputStream bos = new BufferedOutputStream(os,1024)
                        ){
                    bis.transferTo(bos);
                }
                /**
                 * вставляю новый путь, новый размер и новый контенттайп в документ
                 */
                document.setFilePath(newFilePath.toString());
                document.setFileSize(file.getSize());
                document.setMediaType(file.getContentType());
                /**
                 * сохраняю обновленный вокументы в БД
                 */
                documentsForPreparationRepository.save(document);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }

    }

    @Override
    public ResponseEntity<DocumentsForPreparation> getDocument(Integer documentId) {
        if (documentId == 0) {
            return ResponseEntity.badRequest().build();
        } else if (!documentsForPreparationRepository.existsById(documentId)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(documentsForPreparationRepository.findById(documentId).get());
        }
    }

    /**
     * метод ищет по совпадению части описания (description)
     *
     * @param partDescription
     * @return List<' DocumentsForPreparation '>
     * @throws HttpClientErrorException.BadRequest,HttpClientErrorException.NotFound
     * @see DocumentsForPreparationRepository#findByDescriptionContainingIgnoreCase(String)
     */
    @Override
    public ResponseEntity<Collection<DocumentsForPreparation>> getDocuments(String partDescription) {
        if (partDescription == null || partDescription.isEmpty() || partDescription.isBlank()) {
            return ResponseEntity.badRequest().build();
        } else {
            List<DocumentsForPreparation> listDocByPartDiscription =
                    documentsForPreparationRepository.findByDescriptionContainingIgnoreCase(partDescription);
            if (listDocByPartDiscription.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(listDocByPartDiscription);
            }
        }
    }

    @Override
    public ResponseEntity<Collection<DocumentsForPreparation>> getAllDocuments() {
        return ResponseEntity.ok(documentsForPreparationRepository.findAll());
    }

    /**
     * @param documentId
     * @return
     * @throws HttpClientErrorException.BadRequest,HttpClientErrorException.NotFound
     */
    @Override
    public ResponseEntity<Void> removeDocument(Integer documentId) throws IOException{
        if (documentId == 0) {
            return ResponseEntity.badRequest().build();
        } else if (!documentsForPreparationRepository.existsById(documentId)) {
            return ResponseEntity.notFound().build();
        } else {

            DocumentsForPreparation document = documentsForPreparationRepository.findById(documentId).get();
            Path filePath = Path.of(document.getFilePath());
            Files.deleteIfExists(filePath);

            documentsForPreparationRepository.deleteById(documentId);
            return ResponseEntity.ok().build();
        }
    }

}
