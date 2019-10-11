package comp127.weather.api;

import comp127graphics.Image;
import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.Tools;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WeatherData {
    private final CurrentWeather currentConditions;
    private final List<ForecastConditions> hourlyForecasts;

    public WeatherData(OpenWeatherConnection connection) {
        currentConditions = connection.getRawCurrentWeather();
        hourlyForecasts =
            connection.getRawForecast()
                .getForecasts().stream()
                .map(ForecastConditions::new)
                .collect(toList());
    }

    private static final Tools util = new Tools();

    /**
     * Gets the city name corresponding to this weather location
     * @return null if location is not a city
     */
    public String getCityName() {
        if (currentConditions != null && currentConditions.hasCityName()) {
            return currentConditions.getCityName();
        }
        return null;
    }

    /**
     * Gets the current cloud coverage as a percent from 0 to 100%
     * @return (returns 0 % in case of error)
     */
    public double getCurrentCloudCoverage() {
        if (currentConditions != null && currentConditions.hasCloudsInstance() && currentConditions.getCloudsInstance().hasPercentageOfClouds()) {
            return currentConditions.getCloudsInstance().getPercentageOfClouds();
        } else {
            return 0;
        }
    }

    /**
     * Gets the current temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns - 100 in case of error)
     */
    public double getCurrentTemperature() {
        if (currentConditions != null && currentConditions.hasMainInstance() && currentConditions.getMainInstance().hasTemperature()) {
            return currentConditions.getMainInstance().getTemperature();
        } else {
            return -100;
        }
    }

    /**
     * Gets the current atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public double getCurrentPressure() {
        if (currentConditions != null && currentConditions.hasMainInstance() && currentConditions.getMainInstance().hasPressure()) {
            return currentConditions.getMainInstance().getPressure();
        } else {
            return 0;
        }
    }

    /**
     * Gets the current humidity
     * @return (returns 0 in case of error)
     */
    public double getCurrentHumidity() {
        if (currentConditions != null && currentConditions.hasMainInstance() && currentConditions.getMainInstance().hasHumidity()) {
            return currentConditions.getMainInstance().getHumidity();
        } else {
            return 0;
        }
    }

    /**
     * Gets the current windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public double getCurrentWindSpeed() {
        if (currentConditions != null && currentConditions.hasWindInstance() && currentConditions.getWindInstance().hasWindSpeed()) {
            return currentConditions.getWindInstance().getWindSpeed();
        } else {
            return 0;
        }
    }


    /**
     * Gets the current direction of the wind.
     * @return (returns " " in case of error)
     */
    public String getCurrentWindDirection() {
        if (currentConditions != null && currentConditions.hasWindInstance() && currentConditions.getWindInstance().hasWindDegree()) {
            return util.convertDegree2Direction(currentConditions.getWindInstance().getWindDegree());
        } else {
            return "";
        }
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    public double getCurrentWindDegree() {
        if (currentConditions != null && currentConditions.hasWindInstance() && currentConditions.getWindInstance().hasWindDegree()) {
            return currentConditions.getWindInstance().getWindDegree();
        } else {
            return 0.0;
        }
    }

    /**
     * Gets the sunrise time (or null if unknown)
     */
    public Date getSunrise() {
        if (currentConditions != null && currentConditions.hasSysInstance() && currentConditions.getSysInstance().hasSunriseTime()) {
            return currentConditions.getSysInstance().getSunriseTime();
        }
        return null;
    }

    /**
     * Gets the sunset time (or null if unknown)
     */
    public Date getSunset() {
        if (currentConditions != null && currentConditions.hasSysInstance() && currentConditions.getSysInstance().hasSunsetTime()) {
            return currentConditions.getSysInstance().getSunsetTime();
        }
        return null;
    }

    /**
     * Gets a short description of the current weather.
     * Note, if multiple weather conditions are going on at once this only returns the primary weather condition.
     * @return (returns an empty string if unknown or very little of interest is currently going on)
     */
    public String getCurrentWeather() {
        if (currentConditions != null && currentConditions.hasWeatherInstance() && currentConditions.getWeatherCount() > 0 && currentConditions.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = currentConditions.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                return weather.getWeatherDescription();
            }
        }
        return "";
    }

    /**
     * Gets an image representing the current weather.
     * @return
     */
    public Image getWeatherIcon() {
        String file = "";
        if (currentConditions != null && currentConditions.hasWeatherInstance() && currentConditions.getWeatherCount() > 0 && currentConditions.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = currentConditions.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                file = weather.getWeatherIconName();
            }
        }
        file = "/" + file + ".png";
        try {
            return new Image(0, 0, Paths.get(getClass().getResource(file).toURI()).toString());
        } catch (Exception e) {
            try {
                // return the a default image
                return new Image(0, 0, Paths.get(getClass().getResource("/01d.png").toURI()).toString());
            } catch (URISyntaxException e1) {
                // uhh... this shouldn't happen. For some reason we couldn't load the _default_ image.
                // best guess somehow the res folder got unmarked as a resources folder, or the images got renamed.
                e1.printStackTrace();
                throw new RuntimeException(e1);
            }
        }
    }

    /**
     * Returns an array of forecastwrappers holding information about the future forecast
     * @return The array holds data for 5 days. Each ForecastWrapper represents a 3 hour time period.
     */
    public List<ForecastConditions> getForecasts() {
        return Collections.unmodifiableList(hourlyForecasts);
    }
}
