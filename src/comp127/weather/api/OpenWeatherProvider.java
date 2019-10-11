package comp127.weather.api;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private final double lat, lng;

    private final OpenWeatherMap openWeather;

    private static final ExecutorService requestQueue = Executors.newSingleThreadExecutor();

    /**
     * Create a new OpenWeatherConnection object that uses cityname and country code to locate which weather we want.
     * @param apiKey your openWeather api library
     * @param cityName the name of the city you want to
     * @param countryCode the country code for the country you want to get weather from
     */
    public OpenWeatherProvider(String apiKey, String cityName, String countryCode) {
        openWeather = new OpenWeatherMap(apiKey);
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.lat = this.lng = Double.NaN;
        setUnitsFahrenheit();
    }

    /**
     * Create a new OpenWeatherConnection object that uses cityname and country code to locate which weather we want.
     * @param apiKey your openWeather api library
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
            WeatherData result = new WeatherData(
                fetchCurrentForecast(),
                fetchHourlyForecast());

            System.out.println("Got weather data:");
            System.out.println(result);

            SwingUtilities.invokeLater(() ->
                completionCallback.accept(result));
        });
    }

    /**
     * Returns true if we should use the city name to fetch weather info.
     */
    private boolean usingCityName() {
        return cityName != null && countryCode != null;
    }

    private CurrentWeather fetchCurrentForecast() {
        System.out.print("Updating current weather ...");
        CurrentWeather cw = null;
        try {
            if (usingCityName()) {
                cw = openWeather.currentWeatherByCityName(cityName, countryCode);
            } else {
                cw = openWeather.currentWeatherByCoordinates((float) lat, (float) lng);
            }
        } catch (IOException ex) {
            throw new WeatherException("cannot get weather data!", ex);
        }
        if (cw == null) {
            throw new WeatherException("cannot get weather data, and I don't know why.");
        }
        System.out.println(" done.");
        return cw;
    }

    private HourlyForecast fetchHourlyForecast() {
        System.out.print("Updating forecast ...");
        HourlyForecast hf = null;
        try {
            if (usingCityName()) {
                hf = openWeather.hourlyForecastByCityName(cityName, countryCode);
            } else {
                hf = openWeather.hourlyForecastByCoordinates((float) lat, (float) lng);
            }
        } catch (IOException ex) {
            throw new WeatherException("cannot get weather data!", ex);
        }
        if (hf == null) {
            throw new WeatherException("cannot get weather data, and I don't know why.");
        }
        System.out.println(" done.");
        return hf;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        new OpenWeatherProvider("d6a22c9835563a57b372e6515fd8ec2b", 44.9, -93.0)
            .fetchWeather(System.out::println);
    }
}