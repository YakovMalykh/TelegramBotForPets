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

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
@Transactional
public class DocumentsForPreparationServiceImpl implements DocumentsForPreparationService {

    private final Logger logger = LoggerFactory.getLogger(DocumentsForPreparationServiceImpl.class);

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
     * @param kindOfAnimal
     * @return
     * @throws IOException
     */
    @Override
    public boolean saveDocumentToDB(String description, MultipartFile file, String kindOfAnimal) throws IOException {
        DocumentsForPreparation docExists =
                documentsForPreparationRepository.findDocumentsForPreparationByDescriptionAndKindOfAnimal (description, kindOfAnimal);
        

        if (docExists == null) {

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
            document.setKindOfAnimal(kindOfAnimal);
            document.setDescription(description);
            document.setFilePath(filePath.toString());
            document.setFileSize(file.getSize());
            document.setMediaType(file.getContentType());

            documentsForPreparationRepository.save(document);
            logger.info("вызван метода saveDocumentToDB, файл сохранен в БД");
            return true;
        }
        logger.info("вызван метода saveDocumentToDB, файл с таким описанием уже есть в БД");
        return false;
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
     * @param description
     * @param file
     * @param kindOfAnimal
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<Void> editDocuments(String description, MultipartFile file, String kindOfAnimal) throws IOException {
        if (description == null || file == null || kindOfAnimal == null) {
            logger.info("при вызове метода editDocuments переданы некорректные данные");
            return ResponseEntity.badRequest().build();
        } else {
            DocumentsForPreparation document =
                    documentsForPreparationRepository.findDocumentsForPreparationByDescriptionAndKindOfAnimal(description, kindOfAnimal);
            if (document != null) {

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
                try (
                        InputStream is = file.getInputStream();
                        OutputStream os = Files.newOutputStream(newFilePath, CREATE_NEW);
                        BufferedInputStream bis = new BufferedInputStream(is, 1024);
                        BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
                ) {
                    bis.transferTo(bos);
                }
                /**
                 * вставляю новый путь, новый размер и новый контенттайп в документ
                 */
                document.setKindOfAnimal(kindOfAnimal);
                document.setFilePath(newFilePath.toString());
                document.setFileSize(file.getSize());
                document.setMediaType(file.getContentType());
                /**
                 * сохраняю обновленный вокументы в БД
                 */
                documentsForPreparationRepository.save(document);
                logger.info("метод editDocuments - файл в папке и документ в БД обновлены");
                return ResponseEntity.ok().build();
            } else {
                logger.info("метод editDocuments - документа с таким description в БД не существует");
                return ResponseEntity.notFound().build();
            }
        }

    }

    @Override
    public ResponseEntity<DocumentsForPreparation> getDocument(Integer documentId) {
        if (documentId == 0) {
            logger.info("при вызове метода getDocument передан некорректный параметр");
            return ResponseEntity.badRequest().build();
        } else if (!documentsForPreparationRepository.existsById(documentId)) {
            logger.info("метод getDocument - документ с таким documentId в БД не найден");
            return ResponseEntity.notFound().build();
        } else {
            logger.info("метод getDocument - получен документ из БД по documentId");
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
            logger.info("метод getDocuments - переданы некорретные параметры");
            return ResponseEntity.badRequest().build();
        } else {
            List<DocumentsForPreparation> listDocByPartDiscription =
                    documentsForPreparationRepository.findByDescriptionContainingIgnoreCase(partDescription);
            if (listDocByPartDiscription.isEmpty()) {
                logger.info("метод getDocuments - документы по заданным параметрам в БД не найдены");
                return ResponseEntity.notFound().build();
            } else {
                logger.info("метод getDocuments - вернул список документов по части description");
                return ResponseEntity.ok(listDocByPartDiscription);
            }
        }
    }

    @Override
    public ResponseEntity<Collection<DocumentsForPreparation>> getAllDocuments() {
        logger.info("метод getAllDocuments - вернул список всех имеющихся документов в БД");
        return ResponseEntity.ok(documentsForPreparationRepository.findAll());
    }

    /**
     * @param documentId
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity<Void> removeDocument(Integer documentId) throws IOException {
        if (documentId == 0) {
            logger.info("метод removeDocument - передан некоррекнтый параметр");
            return ResponseEntity.badRequest().build();
        } else if (!documentsForPreparationRepository.existsById(documentId)) {
            logger.info("метод removeDocument - документа с таким ID в БД не существует");
            return ResponseEntity.notFound().build();
        } else {

            DocumentsForPreparation document = documentsForPreparationRepository.findById(documentId).get();
            Path filePath = Path.of(document.getFilePath());
            Files.deleteIfExists(filePath);

            documentsForPreparationRepository.deleteById(documentId);
            logger.info("метод removeDocument - файл из папки и документ из БД удалены");
            return ResponseEntity.ok().build();
        }
    }

    @Override
    public ResponseEntity<DocumentsForPreparation> getDocumentsByDescription(String description) {
        return ResponseEntity.ok(documentsForPreparationRepository.findDocumentsForPreparationByDescription(description));
    }

}
