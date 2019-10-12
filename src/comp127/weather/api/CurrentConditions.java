package comp127.weather.api;

import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.Tools;

import java.util.Date;

public class CurrentConditions {
    public static final CurrentConditions BLANK = new CurrentConditions();
    private static final Tools weatherUtils = new Tools();

    private double cloudCoverage = 0;
    private double temperature = -100;
    private double pressure = 0;
    private double humidity = 0;
    private double windSpeed = 0;
    private double windDirectionInDegrees = 0;
    private Date sunrise = null;
    private Date sunset = null;
    private String currentWeather = "";
    private String weatherIconFile = "unknown";

    private CurrentConditions() {
    }

    /**
     * For fetching from API
     */
    CurrentConditions(CurrentWeather rawCurrentConditions) {
        if (rawCurrentConditions.hasCloudsInstance()) {
            cloudCoverage = rawCurrentConditions.getCloudsInstance().getPercentageOfClouds();
        }
        if (rawCurrentConditions.hasMainInstance()) {
            temperature = rawCurrentConditions.getMainInstance().getTemperature();
            pressure = rawCurrentConditions.getMainInstance().getPressure();
            humidity = rawCurrentConditions.getMainInstance().getHumidity();
        }
        if (rawCurrentConditions.hasWindInstance()) {
            windSpeed = rawCurrentConditions.getWindInstance().getWindSpeed();
            windDirectionInDegrees = rawCurrentConditions.getWindInstance().getWindDegree();
        }
        if (rawCurrentConditions.hasSysInstance()) {
            sunrise = rawCurrentConditions.getSysInstance().getSunriseTime();
            sunset = rawCurrentConditions.getSysInstance().getSunsetTime();
        }
        if (rawCurrentConditions.hasWeatherInstance()
                && rawCurrentConditions.getWeatherCount() > 0
                && rawCurrentConditions.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = rawCurrentConditions.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                currentWeather = weather.getWeatherDescription();
                weatherIconFile = weather.getWeatherIconName();
            }
        }
    }

    /**
     * For generating test data
     */
    CurrentConditions(double cloudCoverage, double temperature, double pressure, double humidity, double windSpeed,
            double windDirectionInDegrees, Date sunrise, Date sunset, String currentWeather, String weatherIconFile) {
        this.cloudCoverage = cloudCoverage;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirectionInDegrees = windDirectionInDegrees;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.currentWeather = currentWeather;
        this.weatherIconFile = weatherIconFile;
    }

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
     * Gets the current direction of the wind.
     * @return (returns "" in case of error)
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

    @Override
    public String toString() {
        return "CurrentConditions{" +
            "cloudCoverage=" + cloudCoverage +
            ", temperature=" + temperature +
            ", pressure=" + pressure +
            ", humidity=" + humidity +
            ", windSpeed=" + windSpeed +
            ", windDirectionInDegrees=" + windDirectionInDegrees +
            ", sunrise=" + sunrise +
            ", sunset=" + sunset +
            ", currentWeather='" + currentWeather + '\'' +
            ", weatherIconFile='" + weatherIconFile + '\'' +
            '}';
    }
}