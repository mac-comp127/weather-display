package comp127.weather.api;

import comp127graphics.Image;
import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.Tools;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Date;

/**
 * A class to provide simple access to hourly forecasts.
 * The array of these objects should have 5 days worth of data with one object for every 3 hours.
 */
public class ForecastWrapper {

    private static final Tools util = new Tools();

    private final HourlyForecast.Forecast rawForecast;

    public ForecastWrapper(HourlyForecast.Forecast forecastInstance) {
        this.rawForecast = forecastInstance;
    }


    /**
     * Gets the predicted cloud coverage as a percent from 0 to 100%
     * @return (returns 0% in case of error)
     */
    public double getPredictedCloudCoverage() {
        if (rawForecast != null && rawForecast.hasCloudsInstance() && rawForecast.getCloudsInstance().hasPercentageOfClouds()) {
            return rawForecast.getCloudsInstance().getPercentageOfClouds();
        } else {
            return 0;
        }
    }

    /**
     * Gets the predicted temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    public double getPredictedTemperature() {
        if (rawForecast != null && rawForecast.hasMainInstance() && rawForecast.getMainInstance().hasTemperature()) {
            return rawForecast.getMainInstance().getTemperature();
        } else {
            return -100;
        }
    }

    /**
     * Gets the predicted minimum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     *  @return (returns -100 in case of error)
     */
    public double getPredictedMinTemperature() {
        if (rawForecast != null && rawForecast.hasMainInstance() && rawForecast.getMainInstance().hasMinTemperature()) {
            return rawForecast.getMainInstance().getMinTemperature();
        } else {
            return -100;
        }
    }

    /**
     * Gets the predicted maximum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    public double getPredictedMaxTemperature() {
        if (rawForecast != null && rawForecast.hasMainInstance() && rawForecast.getMainInstance().hasMaxTemperature()) {
            return rawForecast.getMainInstance().getMaxTemperature();
        } else {
            return -100;
        }
    }

    /**
     * Gets the predicted atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public double getPredictedPressure() {
        if (rawForecast != null && rawForecast.hasMainInstance() && rawForecast.getMainInstance().hasPressure()) {
            return rawForecast.getMainInstance().getPressure();
        } else {
            return 0;
        }
    }

    /**
     * Gets the predicted humidity
     * @return (returns 0 in case of error)
     */
    public double getPredictedHumidity() {
        if (rawForecast != null && rawForecast.hasMainInstance() && rawForecast.getMainInstance().hasHumidity()) {
            return rawForecast.getMainInstance().getHumidity();
        } else {
            return 0;
        }
    }

    /**
     * Gets the predicted windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public double getPredictedWindSpeed() {
        if (rawForecast != null && rawForecast.hasWindInstance() && rawForecast.getWindInstance().hasWindSpeed()) {
            return rawForecast.getWindInstance().getWindSpeed();
        } else {
            return 0;
        }
    }


    /**
     * Gets the predicted direction of the wind.
     * @return (returns "" in case of error)
     */
    public String getPredictedWindDirection() {
        if (rawForecast != null && rawForecast.hasWindInstance() && rawForecast.getWindInstance().hasWindDegree()) {
            return util.convertDegree2Direction(rawForecast.getWindInstance().getWindDegree());
        } else {
            return "";
        }
    }

    /**
     * Gets a short description of the predicted weather.
     * Note, if multiple weather conditions are going on at once this only returns the primary weather condition.
     * @return (returns an empty string if unknown or very little of interest is currently going on)
     */
    public String getPredictedWeather() {
        if (rawForecast != null && rawForecast.hasWeatherInstance() && rawForecast.getWeatherCount() > 0 && rawForecast.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawForecast.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                return weather.getWeatherDescription();
            }
        }
        return "";
    }

    /**
     * Gets an image representing the current weather
     * @return
     */
    public Image getWeatherIcon() {
        String file = "";
        if (rawForecast != null && rawForecast.hasWeatherInstance() && rawForecast.getWeatherCount() > 0 && rawForecast.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawForecast.getWeatherInstance(0);
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
     * Gets a date object representing the date/time that this prediction is for.
     * @return
     */
    public Date getPredictionTime() {
        return rawForecast.getDateTime();
    }

}
