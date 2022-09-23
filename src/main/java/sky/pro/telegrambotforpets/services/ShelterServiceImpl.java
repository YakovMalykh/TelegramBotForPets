package sky.pro.telegrambotforpets.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
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
     *
     * @param shelter     JSON
     * @param howToGet    - файл со схемой проезда
     * @param safetyRules - файл с правилами безопасности
     * @return true - когда сохранен успешно, false - когда такой приют уже есть
     * @throws IOException
     * @see ShelterServiceImpl#saveFileToFolder
     */
    @Override
    public boolean saveShelterToDB(Shelter shelter, MultipartFile howToGet, MultipartFile safetyRules) throws IOException {
        String sheltersName = shelter.getName();
        if (shelterRepository.findFirstByNameIgnoreCase(sheltersName) == null) {
            String pathToMap = saveFileToFolder(TypeOfFiles.HOWTOGET, sheltersName, howToGet, aboutShelterFolder);
            String pathToRules = saveFileToFolder(TypeOfFiles.SAFETYRULES, sheltersName, safetyRules, aboutShelterFolder);
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
    private enum TypeOfFiles {
        HOWTOGET, SAFETYRULES
    }

    /**
     * сохраняет файл в папку about-shelter, задает ему нужное название и возвращает путь к этому фалу
     * в виде строки. В название добавил навзвание приюта
     *
     * @param typeOfFiles  - имя выбираем из Enum @NamesOfFiles
     * @param file         - переданный пользователем файл
     * @param pathToFolder - путь к папке, куда сохраняем (прописана в @application.properties)
     * @return путь к файлу в виде строки
     * @throws IOException
     */
    private String saveFileToFolder(
            TypeOfFiles typeOfFiles, String sheltersName, MultipartFile file, String pathToFolder) throws IOException {
        Path filePath = Path.of(pathToFolder, sheltersName,sheltersName + "-" + typeOfFiles.name() + "." + getExtention(file));
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
    public boolean editShelter(Long id, String name, String address, String schedule, KindOfAnimal kindOfAnimal,
                               String description, String securityPhoneNumber, MultipartFile howToGet,
                               MultipartFile safetyRules
    ) throws IOException {
        Shelter shelter = getShelter(id);
        if (shelter != null) {
            if (name != null && !name.isEmpty()) {
                shelter.setName(name);
            }
            if (address != null && !address.isEmpty()) {
                shelter.setAdress(address);
            }
            if (schedule != null && !schedule.isEmpty()) {
                shelter.setSchedule(schedule);
            }
            if (kindOfAnimal != null) {
                shelter.setSpecialization(kindOfAnimal.name());
            }
            if (description != null && !description.isEmpty()) {
                shelter.setDescription(description);
            }
            if (securityPhoneNumber != null && !securityPhoneNumber.isEmpty()) {
                shelter.setSecurityPhoneNumber(securityPhoneNumber);
            }
            if (howToGet != null) {
                String pathToMap = saveFileToFolder(TypeOfFiles.HOWTOGET,shelter.getName(), howToGet, aboutShelterFolder);
                shelter.setMapPath(pathToMap);
            }
            if (safetyRules != null) {
                String pathToRules = saveFileToFolder(TypeOfFiles.SAFETYRULES,shelter.getName(), safetyRules, aboutShelterFolder);
                shelter.setRecomendationPath(pathToRules);
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * ищет приют по id, может быть null
     *
     * @param id
     * @return shelter
     */
    @Override
    public Shelter getShelter(Long id) {
        return shelterRepository.findById(id).get();
    }

    /**
     * возвращает все имеющиесы приюты в БД или пустой список
     *
     * @return List
     */
    @Override
    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }

    @Override
    public boolean removeShelter(Long id) {
        Shelter shelter = getShelter(id);
        if (shelter != null) {
            shelterRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Shelter findShelterBySpecialization(String kindOfAnimal) {
        return shelterRepository.findShelterBySpecialization(kindOfAnimal);
    }
}
