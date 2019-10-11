package comp127.weather.api;

import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.Tools;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WeatherData {
    private String cityName = "";
    private double currentCloudCoverage = 0;
    private double currentTemperature = -100;
    private double currentPressure = 0;
    private double currentHumidity = 0;
    private double currentWindSpeed = 0;
    private String currentWindDirection = "";
    private double currentWindDegree = 0;
    private Date sunrise = null;
    private Date sunset = null;
    private String currentWeather = "";
    private String weatherIconFile = "unknown";

    private final List<ForecastConditions> hourlyForecasts;

    public WeatherData(CurrentWeather currentConditions, HourlyForecast hourlyForecast) {
        if (currentConditions != null) {
            cityName = nullIfBlank(currentConditions.getCityName());
            if (currentConditions.hasCloudsInstance()) {
                currentCloudCoverage = currentConditions.getCloudsInstance().getPercentageOfClouds();
            }
            if (currentConditions.hasMainInstance()) {
                currentTemperature = currentConditions.getMainInstance().getTemperature();
                currentPressure = currentConditions.getMainInstance().getPressure();
                currentHumidity = currentConditions.getMainInstance().getHumidity();
            }
            if (currentConditions.hasWindInstance()) {
                currentWindSpeed = currentConditions.getWindInstance().getWindSpeed();
                currentWindDegree = currentConditions.getWindInstance().getWindDegree();
                if (currentWindDegree >= 0 && currentWindDegree <= 360) {
                    currentWindDirection = util.convertDegree2Direction((float) currentWindDegree);
                }
            }
            if (currentConditions.hasSysInstance()) {
                sunrise = currentConditions.getSysInstance().getSunriseTime();
                sunset = currentConditions.getSysInstance().getSunsetTime();
            }
            if (currentConditions.hasWeatherInstance()
                    && currentConditions.getWeatherCount() > 0
                    && currentConditions.getWeatherInstance(0) != null) {
                AbstractWeather.Weather weather = currentConditions.getWeatherInstance(0);
                if (weather.hasWeatherDescription()) {
                    currentWeather = weather.getWeatherDescription();
                    weatherIconFile = weather.getWeatherIconName();
                }
            }
        }
        this.hourlyForecasts =
            hourlyForecast
                .getForecasts().stream()
                .map(ForecastConditions::new)
                .collect(toList());
    }

    private String nullIfBlank(String str) {
        return (str == null || str.isBlank()) ? null : str;
    }

    private static final Tools util = new Tools();

    /**
     * Gets the city name corresponding to this weather location
     * @return null if location is not a city
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Gets the current cloud coverage as a percent from 0 to 100%
     * @return (returns 0 % in case of error)
     */
    public double getCurrentCloudCoverage() {
        return currentCloudCoverage;
    }

    /**
     * Gets the current temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns - 100 in case of error)
     */
    public double getCurrentTemperature() {
        return currentTemperature;
    }

    /**
     * Gets the current atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public double getCurrentPressure() {
        return currentPressure;
    }

    /**
     * Gets the current humidity
     * @return (returns 0 in case of error)
     */
    public double getCurrentHumidity() {
        return currentHumidity;
    }

    /**
     * Gets the current windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public double getCurrentWindSpeed() {
        return currentWindSpeed;
    }


    /**
     * Gets the current direction of the wind.
     * @return (returns " " in case of error)
     */
    public String getCurrentWindDirection() {
        return currentWindDirection;
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    public double getCurrentWindDegree() {
        return currentWindDegree;
    }

    /**
     * Gets the sunrise time (or null if unknown)
     */
    public Date getSunrise() {
        return sunrise;
    }

    /**
     * Gets the sunset time (or null if unknown)
     */
    public Date getSunset() {
        return sunset;
    }

    /**
     * Gets a short description of the current weather.
     * Note, if multiple weather conditions are going on at once this only returns the primary weather condition.
     * @return (returns an empty string if unknown or very little of interest is currently going on)
     */
    public String getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Returns an image representing the current weather.
     */
    public String getWeatherIcon() {
        return "condition-icons/" + weatherIconFile + ".png";
    }

    /**
     * Returns an array of forecastwrappers holding information about the future forecast
     * @return The array holds data for 5 days. Each ForecastWrapper represents a 3 hour time period.
     */
    public List<ForecastConditions> getForecasts() {
        return Collections.unmodifiableList(hourlyForecasts);
    }
}
