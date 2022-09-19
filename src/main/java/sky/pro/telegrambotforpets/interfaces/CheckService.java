package sky.pro.telegrambotforpets.interfaces;

public interface CheckService {
    /**
     * отправляет в чат усыновителю предупреждение и коммент волонтера по отчету
     * @param chatId
     * @param volunteersComment
     */
    void reportIsPoorlyCompleted(Long chatId, String volunteersComment);
}
