package ewm.common.exception;

/**
 * Исключение для нарушения бизнес-правил, которое соответствует конфликту состояния ресурса.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
