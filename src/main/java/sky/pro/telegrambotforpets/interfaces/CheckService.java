package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.constants.AdoptionsResult;
import sky.pro.telegrambotforpets.model.Adoption;
import sky.pro.telegrambotforpets.model.Report;

import java.util.List;

public interface CheckService {
    /**
     * отправляет в чат усыновителю предупреждение и коммент волонтера по отчету
     *
     * @param chatId
     * @param volunteersComment
     */
    void reportIsPoorlyCompleted(Long chatId, String volunteersComment);

//    /**
//     * проверяет поля отчета и если поле не заполнено - высылает усыновителю сообщение, что его нужно заполнить
//     * @param report
//     */
//    void checksAllFieldsAreFilled(Report report);

    /**
     * отправляет усыновителю сообщение с результатом испытательного срока
     * @param adoption
     * @param adoptionsResult
     */
    void notifications(Adoption adoption, AdoptionsResult adoptionsResult);

    void dailyCheck();
}
