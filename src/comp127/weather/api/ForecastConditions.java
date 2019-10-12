package comp127.weather.api;

import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.Tools;

import java.util.Date;

/**
 * A class to provide simple access to hourly forecasts.
 * The array of these objects should have 5 days worth of data with one object for every 3 hours.
 */
public class ForecastConditions {
    public static final ForecastConditions BLANK = new ForecastConditions();
    private static final Tools weatherUtils = new Tools();

    private Date predictionTime;
    private double cloudCoverage;
    private double temperature;
    private double minTemperature;
    private double maxTemperature;
    private double pressure;
    private double humidity;
    private double windSpeed;
    private double windDirectionInDegrees;
    private String weatherDescription;
    private String weatherIconFile;

    private ForecastConditions() {
    }

    /**
     * For fetching from API
     */
    ForecastConditions(HourlyForecast.Forecast rawForecast) {
        predictionTime = rawForecast.getDateTime();
        if (rawForecast.hasCloudsInstance()) {
            cloudCoverage = rawForecast.getCloudsInstance().getPercentageOfClouds();
        }
        if (rawForecast.hasMainInstance()) {
            temperature = rawForecast.getMainInstance().getTemperature();
            minTemperature = rawForecast.getMainInstance().getMinTemperature();
            maxTemperature = rawForecast.getMainInstance().getMaxTemperature();
            pressure = rawForecast.getMainInstance().getPressure();
            humidity = rawForecast.getMainInstance().getHumidity();
        }
        if (rawForecast.hasWindInstance() && rawForecast.getWindInstance().hasWindSpeed()) {
            windSpeed = rawForecast.getWindInstance().getWindSpeed();
            windDirectionInDegrees = rawForecast.getWindInstance().getWindDegree();
        }
        if (rawForecast.hasWeatherInstance() && rawForecast.getWeatherCount() > 0 && rawForecast.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawForecast.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                weatherDescription = weather.getWeatherDescription();
                weatherIconFile = weather.getWeatherIconName();
            }
        }
    }

    /**
     * Gets the predicted cloud coverage as a percent from 0 to 100%
     * @return (returns 0% in case of error)
     */
    public double getCloudCoverage() {
        return cloudCoverage;
    }

    /**
     * Gets the predicted temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gets the predicted minimum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     *  @return (returns -100 in case of error)
     */
    public double getMinTemperature() {
        return minTemperature;
    }

    /**
     * Gets the predicted maximum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    public double getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * Gets the predicted atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * Gets the predicted humidity
     * @return (returns 0 in case of error)
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Gets the predicted windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    public double getWindDirectionInDegrees() {
        return windDirectionInDegrees;
    }

    /**
     * Gets a description of the direction of the wind, such as "S" or "NNW".
     * @return "" in case of error
     */
    public String getPredictedWindDirectionAsString() {
        if (getWindDirectionInDegrees() >= 0 && getWindDirectionInDegrees() <= 360) {
            return weatherUtils.convertDegree2Direction((float) getWindDirectionInDegrees());
        } else {
            return "";
        }
    }

    /**
     * Gets a short description of the predicted weather.
     * Note, if multiple weather conditions are going on at once this only returns the primary weather condition.
     * @return (returns an empty string if unknown or very little of interest is currently going on)
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    /**
     * Gets an image representing the current weather
     */
    public String getWeatherIcon() {
        return "condition-icons/" + weatherIconFile + ".png";
    }

    /**
     * Returns the moment in time that this prediction is for.
     */
    public Date getPredictionTime() {
        return predictionTime;
    }

    @Override
    public String toString() {
        return "ForecastConditions{"
            + "predictionTime=" + predictionTime
            + ", cloudCoverage=" + cloudCoverage
            + ", temperature=" + temperature
            + ", minTemperature=" + minTemperature
            + ", maxTemperature=" + maxTemperature
            + ", pressure=" + pressure
            + ", humidity=" + humidity
            + ", windSpeed=" + windSpeed
            + ", windDirectionInDegrees=" + windDirectionInDegrees
            + ", weatherDescription='" + weatherDescription + '\''
            + ", weatherIconFile='" + weatherIconFile + '\''
            + '}';
    }
}
