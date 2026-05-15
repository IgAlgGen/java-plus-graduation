package ewm.common.exception;

/**
 * Исключение для ситуаций, когда запрошенный ресурс не найден или недоступен текущему пользователю.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
