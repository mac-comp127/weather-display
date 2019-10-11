package comp127.weather.widgets;

import comp127.weather.api.OpenWeatherProvider;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

/**
 * This example shows how to create a weather connection and initialize a weather widget.
 * Created by dkluver on 10/6/17.
 */
public class SimpleWidgetExample implements WeatherWidget {

    private GraphicsGroup group;
    private Image weatherIcon;

    public SimpleWidgetExample() {
        group = new GraphicsGroup();

        weatherIcon = new Image(50, 50);
        group.add(weatherIcon);
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void onHover(Point position) {
    }

    public void update(WeatherData data) {
        weatherIcon.setImagePath(data.getWeatherIcon());

        // Examples of how to get other weather data:
        System.out.println(data.getCityName());
        System.out.println(data.getCurrentCloudCoverage());
        System.out.println(data.getCurrentTemperature());
        System.out.println(data.getCurrentPressure());
        System.out.println(data.getCurrentHumidity());
        System.out.println(data.getCurrentWindSpeed());
        System.out.println(data.getCurrentWindDirection());
        System.out.println(data.getSunrise());
        System.out.println(data.getSunset());
        System.out.println(data.getCurrentWeather());
    }
}
