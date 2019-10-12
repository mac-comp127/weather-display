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
    private double predictedCloudCoverage;
    private double predictedTemperature;
    private double predictedMinTemperature;
    private double predictedMaxTemperature;
    private double predictedPressure;
    private double predictedHumidity;
    private double predictedWindSpeed;
    private double predictedWindDirectionInDegrees;
    private String predictedWeather;
    private String weatherIconFile;

    private ForecastConditions() {
    }

    /**
     * For fetching from API
     */
    ForecastConditions(HourlyForecast.Forecast rawForecast) {
        predictionTime = rawForecast.getDateTime();
        if (rawForecast.hasCloudsInstance()) {
            predictedCloudCoverage = rawForecast.getCloudsInstance().getPercentageOfClouds();
        }
        if (rawForecast.hasMainInstance()) {
            predictedTemperature = rawForecast.getMainInstance().getTemperature();
            predictedMinTemperature = rawForecast.getMainInstance().getMinTemperature();
            predictedMaxTemperature = rawForecast.getMainInstance().getMaxTemperature();
            predictedPressure = rawForecast.getMainInstance().getPressure();
            predictedHumidity = rawForecast.getMainInstance().getHumidity();
        }
        if (rawForecast.hasWindInstance() && rawForecast.getWindInstance().hasWindSpeed()) {
            predictedWindSpeed = rawForecast.getWindInstance().getWindSpeed();
            predictedWindDirectionInDegrees = rawForecast.getWindInstance().getWindDegree();
        }
        if (rawForecast.hasWeatherInstance() && rawForecast.getWeatherCount() > 0 && rawForecast.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawForecast.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                predictedWeather = weather.getWeatherDescription();
                weatherIconFile = weather.getWeatherIconName();
            }
        }
    }


    /**
     * Gets the predicted cloud coverage as a percent from 0 to 100%
     * @return (returns 0% in case of error)
     */
    public double getPredictedCloudCoverage() {
        return predictedCloudCoverage;
    }

    /**
     * Gets the predicted temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    public double getPredictedTemperature() {
        return predictedTemperature;
    }

    /**
     * Gets the predicted minimum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     *  @return (returns -100 in case of error)
     */
    public double getPredictedMinTemperature() {
        return predictedMinTemperature;
    }

    /**
     * Gets the predicted maximum temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    public double getPredictedMaxTemperature() {
        return predictedMaxTemperature;
    }

    /**
     * Gets the predicted atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public double getPredictedPressure() {
        return predictedPressure;
    }

    /**
     * Gets the predicted humidity
     * @return (returns 0 in case of error)
     */
    public double getPredictedHumidity() {
        return predictedHumidity;
    }

    /**
     * Gets the predicted windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public double getPredictedWindSpeed() {
        return predictedWindSpeed;
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    public double getPredictedWindDirectionInDegrees() {
        return predictedWindDirectionInDegrees;
    }

    /**
     * Gets a description of the direction of the wind, such as "S" or "NNW".
     * @return "" in case of error
     */
    public String getPredictedWindDirectionAsString() {
        if (getPredictedWindDirectionInDegrees() >= 0 && getPredictedWindDirectionInDegrees() <= 360) {
            return weatherUtils.convertDegree2Direction((float) getPredictedWindDirectionInDegrees());
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
        return predictedWeather;
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
            + ", predictedCloudCoverage=" + predictedCloudCoverage
            + ", predictedTemperature=" + predictedTemperature
            + ", predictedMinTemperature=" + predictedMinTemperature
            + ", predictedMaxTemperature=" + predictedMaxTemperature
            + ", predictedPressure=" + predictedPressure
            + ", predictedHumidity=" + predictedHumidity
            + ", predictedWindSpeed=" + predictedWindSpeed
            + ", predictedWindDirectionInDegrees=" + predictedWindDirectionInDegrees
            + ", predictedWeather='" + predictedWeather + '\''
            + ", weatherIconFile='" + weatherIconFile + '\''
            + '}';
    }
}
