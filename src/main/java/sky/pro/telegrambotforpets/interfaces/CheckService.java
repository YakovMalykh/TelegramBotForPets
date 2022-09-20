package sky.pro.telegrambotforpets.interfaces;

import sky.pro.telegrambotforpets.model.Report;

public interface CheckService {
    /**
     * отправляет в чат усыновителю предупреждение и коммент волонтера по отчету
     * @param chatId
     * @param volunteersComment
     */
    void reportIsPoorlyCompleted(Long chatId, String volunteersComment);

    void checksAllFieldsAreFilled(Report report);
}
