package comp127.weather.api;

import net.aksingh.owmjapis.Tools;

/**
 * Weather information shared by both current conditions and future forecasts.
 */
public abstract class Conditions {
    private static final Tools weatherUtils = new Tools();

    protected Double cloudCoverage;
    protected Double temperature;
    protected Double pressure;
    protected Double humidity;
    protected Double windSpeed;
    protected Double windDirectionInDegrees;
    protected String weatherDescription;
    protected String weatherIconFile = "unknown";

    protected static Double nullIfNaN(double value) {
        return Double.isNaN(value) ? null : value;
    }

    /**
     * Gets the current cloud coverage as a percent from 0 to 100%
     * @return (returns 0 % in case of error)
     */
    public Double getCloudCoverage() {
        return cloudCoverage;
    }

    /**
     * Gets the current temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns - 100 in case of error)
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * Gets the current atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * Gets the current humidity
     * @return (returns 0 in case of error)
     */
    public Double getHumidity() {
        return humidity;
    }

    /**
     * Gets the current windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public Double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Gets a description of the direction of the wind, such as "S" or "NNW".
     * @return "" in case of error
     */
    public String getWindDirectionAsString() {
        if (windDirectionInDegrees != null && windDirectionInDegrees >= 0 && windDirectionInDegrees <= 360) {
            return weatherUtils.convertDegree2Direction(windDirectionInDegrees.floatValue());
        } else {
            return null;
        }
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    public Double getWindDirectionInDegrees() {
        return windDirectionInDegrees;
    }

    /**
     * Gets a short description of the current weather.
     * Note, if multiple weather conditions are going on at once this only returns the primary weather condition.
     * @return (returns an empty string if unknown or very little of interest is currently going on)
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    /**
     * Returns an image representing the current weather. Never returns null; if the weather
     * conditions are missing or unknown, returns an "unknown" icon.
     */
    public String getWeatherIcon() {
        return "condition-icons/" + weatherIconFile + ".png";
    }
}
