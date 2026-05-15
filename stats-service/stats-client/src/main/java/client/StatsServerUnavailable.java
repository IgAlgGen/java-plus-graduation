package client;

/**
 * Исключение клиента статистики при недоступности stats-server или ошибке обмена с ним.
 */
public class StatsServerUnavailable extends RuntimeException {
    public StatsServerUnavailable(String message) {
        super(message);
    }

    public StatsServerUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
