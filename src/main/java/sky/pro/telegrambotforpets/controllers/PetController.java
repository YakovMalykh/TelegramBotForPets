package sky.pro.telegrambotforpets.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sky.pro.telegrambotforpets.constants.Gender;
import sky.pro.telegrambotforpets.constants.KindOfAnimal;
import sky.pro.telegrambotforpets.interfaces.PetService;
import sky.pro.telegrambotforpets.model.Pet;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final Logger logger = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Void> savePetToDB(
            @RequestParam String name,
            @Parameter(description = "требуется дата вида dd.MM.yyyy")
            @RequestParam String birthDay,
            @RequestParam Gender gender,
            @RequestParam Long breedId,
            @RequestParam Boolean sterilized,
            @RequestParam Boolean invalid,
            @RequestParam KindOfAnimal kindOfAnimal,
            @RequestParam Long shelterId) {
        petService.savePetToDB(name, birthDay,gender,breedId,sterilized,invalid,kindOfAnimal,shelterId);
        return null;
    }


}
