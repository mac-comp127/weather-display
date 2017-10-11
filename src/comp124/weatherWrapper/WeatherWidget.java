package comp124.weatherWrapper;

import comp124graphics.GraphicsGroup;
import comp124graphics.Image;
import net.aksingh.owmjapis.AbstractWeather;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.HourlyForecast;
import net.aksingh.owmjapis.Tools;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Date;

/**
 * A parent class for widgets based on the open weather API.
 */
public abstract class WeatherWidget extends GraphicsGroup {

    private static final Tools util = new Tools();

    private final CurrentWeather current;
    private final HourlyForecast rawForecast;
    private final ForecastWrapper[] forecast;

    public WeatherWidget(OpenWeatherConnection connection) {
        current = connection.getRawCurrentWeather();
        rawForecast = connection.getRawForecast();

        forecast = new ForecastWrapper[rawForecast.getForecastCount()];
        for(int i = 0; i<rawForecast.getForecastCount(); i++) {
            forecast[i] = new ForecastWrapper(rawForecast.getForecastInstance(i));
        }

    }

    /**
     * Draws the graphical representation of the widget
     */
    protected abstract void draw();

    /**
     * Gets the current cloud coverage as a percent from 0 to 100%
     * @return (returns 0% in case of error)
     */
    protected double getCurrentCloudCoverage() {
        if(current != null && current.hasCloudsInstance() && current.getCloudsInstance().hasPercentageOfClouds()) {
            return current.getCloudsInstance().getPercentageOfClouds();
        } else {
            return 0;
        }
    }

    /**
     * Gets the current temperature in whatever unit the openWeatherConnection is set to (Default fahrenheit)
     * @return (returns -100 in case of error)
     */
    protected double getCurrentTemperature() {
        if(current != null && current.hasMainInstance() && current.getMainInstance().hasTemperature()) {
            return current.getMainInstance().getTemperature();
        } else {
            return -100;
        }
    }

    /**
     * Gets the current atmospheric pressure
     * @return (returns 0 in case of error)
     */
    protected double getCurrentPressure() {
        if(current != null && current.hasMainInstance() && current.getMainInstance().hasPressure()) {
            return current.getMainInstance().getPressure();
        } else {
            return 0;
        }
    }

    /**
     * Gets the current humidity
     * @return (returns 0 in case of error)
     */
    protected double getCurrentHumidity() {
        if(current != null && current.hasMainInstance() && current.getMainInstance().hasHumidity()) {
            return current.getMainInstance().getHumidity();
        } else {
            return 0;
        }
    }

    /**
     * Gets the current windSpeed (in miles/second or meters/second depending on your choice of units.
     * @return (returns 0 in case of error)
     */
    protected double getCurrentWindSpeed() {
        if(current != null && current.hasWindInstance() && current.getWindInstance().hasWindSpeed()) {
            return current.getWindInstance().getWindSpeed();
        } else {
            return 0;
        }
    }



    /**
     * Gets the current direction of the wind.
     * @return (returns "" in case of error)
     */
    protected String getCurrentWindDirection() {
        if(current != null && current.hasWindInstance() && current.getWindInstance().hasWindDegree()) {
            return util.convertDegree2Direction(current.getWindInstance().getWindDegree());
        } else {
            return "";
        }
    }

    /**
     * Gets the wind direction reported as degrees off of north.
     * @return wind degrees or 0 in the case of error.
     */
    protected double getCurrentWindDegree(){
        if(current != null && current.hasWindInstance() && current.getWindInstance().hasWindDegree()) {
            return current.getWindInstance().getWindDegree();
        } else {
            return 0.0;
        }
    }

    /**
     * Gets the sunrise time (or null if unknown)
     */
    protected Date getSunrise() {
        if(current != null && current.hasSysInstance() && current.getSysInstance().hasSunriseTime()) {
            return current.getSysInstance().getSunriseTime();
        }
        return null;
    }

    /**
     * Gets the sunset time (or null if unknown)
     */
    protected Date getSunset() {
        if(current != null && current.hasSysInstance() && current.getSysInstance().hasSunsetTime()) {
            return current.getSysInstance().getSunsetTime();
        }
        return null;
    }

    /**
     * Gets a short description of the current weather.
     * Note, if multiple weather conditions are going on at once this only returns the primary weather condition.
     * @return (returns an empty string if unknown or very little of interest is currently going on)
     */
    protected String getCurrentWeather() {
        if (current != null && current.hasWeatherInstance() && current.getWeatherCount() > 0 && current.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = current.getWeatherInstance(0);
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
    protected Image getWeatherIcon() {
        String file = "";
        if (current != null && current.hasWeatherInstance() && current.getWeatherCount() > 0 && current.getWeatherInstance(0) != null) {
            AbstractWeather.Weather weather = current.getWeatherInstance(0);
            if (weather.hasWeatherDescription()) {
                file =  weather.getWeatherIconName();
            }
        }
        file = "/"+file+".png";
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
    protected ForecastWrapper[] getForecast(){
        return forecast;
    }

}
