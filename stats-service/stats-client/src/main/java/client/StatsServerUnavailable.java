package client;

/**
 * Исключение клиента статистики при недоступности gRPC stats-сервисов или ошибке обмена с ними.
 */
public class StatsServerUnavailable extends RuntimeException {
    public StatsServerUnavailable(String message) {
        super(message);
    }

    public StatsServerUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
