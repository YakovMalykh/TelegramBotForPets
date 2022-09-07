package sky.pro.telegrambotforpets.services;

import liquibase.pro.packaged.F;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.interfaces.ShelterService;
import sky.pro.telegrambotforpets.model.Shelter;
import sky.pro.telegrambotforpets.repositories.ShelterRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class ShelterServiceImpl implements ShelterService {

    private Logger logger = LoggerFactory.getLogger(ShelterServiceImpl.class);

    @Value("${path.to.about-shelter.folder}")
    private String aboutShelterFolder;
    private final ShelterRepository shelterRepository;

    public ShelterServiceImpl(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * сохраняет приют в БД
     * @param shelter JSON
     * @param howToGet - файл со схемой проезда
     * @param safetyRules - файл с правилами безопасности
     * @return true - когда сохранен успешно, false - когда такой приют уже есть
     * @throws IOException
     * @see ShelterServiceImpl#saveFileToFolder(NamesOfFiles, MultipartFile, String)
     */
    @Override
    public boolean saveShelterToDB(Shelter shelter, MultipartFile howToGet, MultipartFile safetyRules) throws IOException {
        String sheltersName = shelter.getName();
        if (!shelterRepository.findAll().contains(sheltersName)) {
            String pathToMap = saveFileToFolder(NamesOfFiles.HOWTOGET, howToGet, aboutShelterFolder);
            String pathToRules = saveFileToFolder(NamesOfFiles.SAFETYRULES, safetyRules, aboutShelterFolder);
            shelter.setMapPath(pathToMap);
            shelter.setRecomendationPath(pathToRules);
            shelterRepository.save(shelter);
            logger.info("метод saveShelterToDB - приют " + sheltersName + " сохранен в БД");
            return true;
        } else {
            logger.info("метод saveShelterToDB - приют с названием - " + sheltersName + " уже есть в БД");
            return false;
        }
    }

    /**
     * ввожу Enum для ограниения названий файлов
     */
    private enum NamesOfFiles {
        HOWTOGET, SAFETYRULES
    }

    /**
     * сохраняет файл в папку about-shelter, задает ему нужное название и возвращает путь к этому фалу
     * в виде строки
     *
     * @param namesOfFile  - имя выбираем из Enum @NamesOfFiles
     * @param file         - переданный пользователем файл
     * @param pathToFolder - путь к папке, куда сохраняем (прописана в @application.properties)
     * @return путь к файлу в виде строки
     * @throws IOException
     */
    private String saveFileToFolder(
            NamesOfFiles namesOfFile, MultipartFile file, String pathToFolder) throws IOException {
        Path filePath = Path.of(pathToFolder, namesOfFile.name() + "." + getExtention(file));
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
        logger.info("метод saveFileToFolder - файл сохранен -  " + filePath.toString());
        return filePath.toString();
    }
    private String getExtention(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public boolean editShelter(Shelter shelter) {

        return false;
    }

    @Override
    public Shelter getShelter(Long id) {
        return null;
    }

    @Override
    public List<Shelter> getAllShelters() {
        return null;
    }

    @Override
    public boolean removeShelter(Long id) {
        return false;
    }
}
