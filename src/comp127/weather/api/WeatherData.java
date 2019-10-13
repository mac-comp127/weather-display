package comp127.weather.api;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.HourlyForecast;

import java.util.Collections;
import java.util.DoubleSummaryStatistics;
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
        addUncertainty(hourlyForecasts);
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
     * Increases the temperature range in the hourly forecast to reflect forecast uncertainty,
     * based on (1) range of nearby temperatures and (2) time in the future.
     */
    private void addUncertainty(List<ForecastConditions> forecasts) {
        if (forecasts.isEmpty()) {
            return;
        }
        if (forecasts.stream()
            .anyMatch((f) ->
                f.getPredictionTime() == null
                || f.getTemperature() == null)) {
            return;
        }

        // Uncertainty based on time
        for (ForecastConditions forecast : forecasts) {
            forecast.addUncertainty(
                Math.sqrt(hoursDifference(forecast, forecasts.get(0))) / 3);
        }

        // Uncertainty based on nearby variation
        for (ForecastConditions forecast : forecasts) {
            long forecastTime = forecast.getPredictionTime().getTime();
            DoubleSummaryStatistics stats = forecasts.stream()
                .filter(otherForecast -> hoursDifference(forecast, otherForecast) < 6)
                .mapToDouble(Conditions::getTemperature)
                .summaryStatistics();
            forecast.addUncertainty(
                Math.pow(stats.getMax() - stats.getMin(), 0.8));
        }
    }

    private double hoursDifference(ForecastConditions f0, ForecastConditions f1) {
        return Math.abs(f0.getPredictionTime().getTime() - f1.getPredictionTime().getTime())
             / (3_600_000.0);
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
