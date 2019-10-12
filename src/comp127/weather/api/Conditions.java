package comp127.weather.api;

import net.aksingh.owmjapis.Tools;

public abstract class Conditions {
    private static final Tools weatherUtils = new Tools();

    protected double cloudCoverage = 0;
    protected double temperature = -100;
    protected double pressure = 0;
    protected double humidity = 0;
    protected double windSpeed = 0;
    protected double windDirectionInDegrees = 0;
    protected String weatherDescription = "";
    protected String weatherIconFile = "unknown";

    /**
     * Gets the current cloud coverage as a percent from 0 to 100%
     * @return (returns 0 % in case of error)
     */
    public double getCloudCoverage() {
        return cloudCoverage;
    }

    /**
     * Gets the current temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns - 100 in case of error)
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gets the current atmospheric pressure
     * @return (returns 0 in case of error)
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * Gets the current humidity
     * @return (returns 0 in case of error)
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Gets the current windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Gets a description of the direction of the wind, such as "S" or "NNW".
     * @return "" in case of error
     */
    public String getWindDirectionAsString() {
        if (getWindDirectionInDegrees() >= 0 && getWindDirectionInDegrees() <= 360) {
            return weatherUtils.convertDegree2Direction((float) getWindDirectionInDegrees());
        } else {
            return "";
        }
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    public double getWindDirectionInDegrees() {
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
     * Returns an image representing the current weather.
     */
    public String getWeatherIcon() {
        return "condition-icons/" + weatherIconFile + ".png";
    }
}
