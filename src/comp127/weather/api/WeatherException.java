package comp127.weather.api;

/**
 * A class to represent everything that could possibly go wrong while getting weather.
 *
 * This is a runtime exception because we haven't really covered try/catch in class.
 */
public class WeatherException extends RuntimeException {
    public WeatherException() {
    }

    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherException(Throwable cause) {
        super(cause);
    }

    public WeatherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
