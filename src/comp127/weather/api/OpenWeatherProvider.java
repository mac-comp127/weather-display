package comp127.weather.api;

import net.aksingh.owmjapis.OpenWeatherMap;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Fetches weather data from the OpenWeather API.
 *
 * To create one of these objects you need to have an OpenWeather API key. Instructions for how to
 * get one of these is provided in the instructions for this assignment. You also need to specify a
 * location You can either do that through latitude and longitude OR city name and country code.
 *
 * This class is a custom wrapper over aksingh’s OpenWeatherMap class, providing a simpler and
 * tidier API for the weather widgets homework.
 */
public class OpenWeatherProvider {
    // There are two (well 4, but I'm not implementing all that) ways to specify where where you want
    // weather for. if cityName & stateName are non-null we will use those for every api call, otherwise we
    // will use lat/long.
    private final String cityName, countryCode;
    private final Double lat, lng;

    private final OpenWeatherMap openWeather;

    private static final ExecutorService requestQueue = Executors.newSingleThreadExecutor();

    /**
     * @param apiKey your openWeather api library
     * @param cityName the name of the city you want to
     * @param countryCode the country code for the country you want to get weather from
     */
    public OpenWeatherProvider(String apiKey, String cityName, String countryCode) {
        openWeather = new OpenWeatherMap(apiKey);
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.lat = this.lng = null;
        setUnitsFahrenheit();
    }

    /**
     *
     * @param apiKey
     * @param latitude
     * @param longitude
     */
    public OpenWeatherProvider(String apiKey, double latitude, double longitude) {
        openWeather = new OpenWeatherMap(apiKey);
        this.lat = latitude;
        this.lng = longitude;
        this.cityName = this.countryCode = null;
        setUnitsFahrenheit();
    }

    /**
     * Set the interface to use fahrenheit units
     */
    public void setUnitsFahrenheit() {
        openWeather.setUnits(OpenWeatherMap.Units.IMPERIAL);
    }

    /**
     * Set the interface to use celsius units
     */
    public void setUnitsCelcius() {
        openWeather.setUnits(OpenWeatherMap.Units.METRIC);
    }

    /**
     * Request the object to fetch up to date weather from the servers.
     */
    public void fetchWeather(Consumer<WeatherData> completionCallback) {
        requestQueue.submit(() -> {
            try {
                WeatherData result = new WeatherData(
                    fetch("current conditions",
                        openWeather::currentWeatherByCityName,
                        openWeather::currentWeatherByCoordinates),
                    fetch("hourly forecast",
                        openWeather::hourlyForecastByCityName,
                        openWeather::hourlyForecastByCoordinates));

                System.out.println("Got weather data: " + result);

                SwingUtilities.invokeLater(() ->
                    completionCallback.accept(result));
            } catch (WeatherException e) {
                System.out.println("Unable to fetch weather: " + e);
            }
        });
    }

    private <T> T fetch(
            String requestName,
            APIRequest<String, String, T> cityRequest,
            APIRequest<Float, Float, T> coordinateRequest)
        throws WeatherException {

        System.out.println("Updating " + requestName + " ...");
        T result;
        try {
            if (usingCityName()) {
                result = cityRequest.request(cityName, countryCode);
            } else {
                result = coordinateRequest.request(lat.floatValue(), lng.floatValue());
            }
        } catch (IOException ex) {
            throw new WeatherException("Weather API request failed", ex);
        }
        if (result == null) {
            throw new WeatherException("Could not parse weather API response");
        }
        System.out.println("Done.");
        return result;
    }

    /**
     * Returns true if we should use the city name to fetch weather info.
     */
    private boolean usingCityName() {
        if (cityName != null && countryCode != null) {
            return true;
        }
        if (lat != null && lng != null) {
            return false;
        }
        throw new IllegalStateException("Insufficient location information");
    }

    private interface APIRequest<Arg0, Arg1, Data> {
        Data request(Arg0 arg0, Arg1 arg1) throws IOException;
    }

    public static void main(String[] args) {
        new OpenWeatherProvider("d6a22c9835563a57b372e6515fd8ec2b", 44.9, -93.0)
            .fetchWeather(System.out::println);
    }
}
