package sky.pro.telegrambotforpets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.telegrambotforpets.model.DocumentsForPreparation;

import java.util.List;

public abstract interface DocumentsForPreparationRepository extends JpaRepository<DocumentsForPreparation, Integer> {

    List<DocumentsForPreparation> findByDescriptionContainingIgnoreCase (String partDescription);
    DocumentsForPreparation findFirstByDescriptionIgnoreCase(String description);

    DocumentsForPreparation findDocumentsForPreparationByDescription (String description);

    DocumentsForPreparation findDocumentsForPreparationByDescriptionAndKindOfAnimal (String description, String kindOfAnimal);
}
