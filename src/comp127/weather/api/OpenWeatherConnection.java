package comp127.weather.api;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This class represents a connection to the open weather servers, and is needed to create weather data
 * This class has methods which you shouldn't need to use that allow gathering data from the OpenWeather API.
 * This class also handles throttling to prevent your API key getting blocked by the OpenWeather class.
 *
 * To create one of these objects you need to have an OpenWeather API key. Instructions for how to get
 * one of these is provided in the instructions for this assignment. You also need to specify a location
 * You can either do that through latitude and longitude OR city name and country code.
 *
 * Strictly this class is a custom wrapper over the OpenWeatherMap class, and doesn't actually maintain
 * an active connection. You don't really need to know that though.
 */
public class OpenWeatherConnection {
    // There are two (well 4, but I'm not implementing all that) ways to specify where where you want
    // weather for. if cityName & stateName are non-null we will use those for every api call, otherwise we
    // will use lat/long.
    private String cityName = null, countryCode = null;
    private double lat, lng;

    private OpenWeatherMap baseObject;

    // These variables cache the various forecast results so that we do not re-request them if we don't need to.
    // To explicity request the data to be updated use update();
    private CurrentWeather tmpCurrentWeather = null;
    private HourlyForecast tmpForecast = null;


    /**
     * Create a new OpenWeatherConnection object that uses cityname and country code to locate which weather we want.
     * @param apiKey your openWeather api library
     * @param cityName the name of the city you want to
     * @param countryCode the country code for the country you want to get weather from
     */
    public OpenWeatherConnection(String apiKey, String cityName, String countryCode) {
        baseObject = new OpenWeatherMap(apiKey);
        this.cityName = cityName;
        this.countryCode = countryCode;
        setUnitsFahrenheit();
    }

    /**
     * Create a new OpenWeatherConnection object that uses cityname and country code to locate which weather we want.
     * @param apiKey your openWeather api library
     */
    public OpenWeatherConnection(String apiKey, double latitude, double longitude) {
        baseObject = new OpenWeatherMap(apiKey);
        this.lat = latitude;
        this.lng = longitude;
        setUnitsFahrenheit();
    }


    /**
     * Set the interface to use fahrenheit units
     */
    public void setUnitsFahrenheit() {
        baseObject.setUnits(OpenWeatherMap.Units.IMPERIAL);
    }

    /**
     * Set the interface to use celsius units
     */
    public void setUnitsCelcius() {
        baseObject.setUnits(OpenWeatherMap.Units.METRIC);
    }

    /**
     * Request the object to fetch up to date weather from the servers.
     */
    public void update() {
        updateCurrentForecast();
        updateHourlyForecast();
    }

    /**
     * You probably don't need to call this method.
     *
     * @return a direct view over the current weather as returned by the weather API.
     */
    public CurrentWeather getRawCurrentWeather() {
        if (tmpCurrentWeather == null) {
            update();
        }
        return tmpCurrentWeather;
    }

    /**
     * You probably don't need to call this method.
     *
     * @return a direct view over the hourly weather as returned by the weather API.
     */
    public HourlyForecast getRawForecast() {
        if (tmpForecast == null) {
            update();
        }
        return tmpForecast;
    }

    /**
     * Check if its reasonable to use the cityName, countryCode based weather functions.
     */
    private boolean useCityName() {
        return cityName != null && countryCode != null;
    }

    private void updateCurrentForecast() {
        System.out.print("Updating current weather ...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CurrentWeather cw = null;
        try {
            if (useCityName()) {
                cw = baseObject.currentWeatherByCityName(cityName, countryCode);
            } else {
                cw = baseObject.currentWeatherByCoordinates((float) lat, (float) lng);
            }
        } catch (IOException ex) {
            throw new WeatherException("cannot get weather data!", ex);
        }
        if (cw == null) {
            throw new WeatherException("cannot get weather data, and I don't know why.");
        }
        tmpCurrentWeather = cw;
        System.out.println(" done.");
    }

    private void updateHourlyForecast() {
        System.out.print("Updating forecast ...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HourlyForecast hf = null;
        try {
            if (useCityName()) {
                hf = baseObject.hourlyForecastByCityName(cityName, countryCode);
            } else {
                hf = baseObject.hourlyForecastByCoordinates((float) lat, (float) lng);
            }
        } catch (IOException ex) {
            throw new WeatherException("cannot get weather data!", ex);
        }
        if (hf == null) {
            throw new WeatherException("cannot get weather data, and I don't know why.");
        }
        tmpForecast = hf;
        System.out.println(" done.");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        OpenWeatherConnection tmp = new OpenWeatherConnection("d6a22c9835563a57b372e6515fd8ec2b", 44.9, -93.0);
        System.out.println(tmp.getRawForecast().getRawResponse());
    }
}