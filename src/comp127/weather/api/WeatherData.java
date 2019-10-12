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
    private final CurrentConditions currentConditions;
    private String cityName;

    private final List<ForecastConditions> hourlyForecasts;

    /**
     * For getting API results
     */
    WeatherData(CurrentWeather rawCurrentConditions, HourlyForecast hourlyForecast) {
        if (rawCurrentConditions != null) {
            cityName = nullIfBlank(rawCurrentConditions.getCityName());
            currentConditions = new CurrentConditions(rawCurrentConditions);
        } else {
            currentConditions = CurrentConditions.BLANK;
        }
        this.hourlyForecasts =
            hourlyForecast
                .getForecasts().stream()
                .map(ForecastConditions::new)
                .collect(toList());
    }

    /**
     * For testing
     */
    WeatherData(String cityName, CurrentConditions currentConditions, List<ForecastConditions> hourlyForecasts) {
        this.cityName = cityName;
        this.currentConditions = currentConditions;
        this.hourlyForecasts = hourlyForecasts;
    }

    private String nullIfBlank(String str) {
        return (str == null || str.isBlank()) ? null : str;
    }

    /**
     * Gets the name of the location where these weather conditions are from.
     *
     * @return null if location is not a city
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Returns information about the weather conditions right now.
     */
    public CurrentConditions getCurrentConditions() {
        return currentConditions;
    }

    /**
     * Returns forecasts of conditions at various times in the future.
     *
     * @return Data for up to 5 days at 3-hour time intervals, in chronological order
     */
    public List<ForecastConditions> getForecasts() {
        return Collections.unmodifiableList(hourlyForecasts);
    }

    @Override
    public String toString() {
        return "WeatherData{" +
            "currentConditions=" + currentConditions +
            ", cityName='" + cityName + '\'' +
            ", hourlyForecasts=" + hourlyForecasts +
            '}';
    }
}
