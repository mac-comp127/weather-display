package comp127.weather.widgets;

import comp127.weather.api.WeatherData;
import comp127graphics.*;

/**
 * This example shows how to create a weather connection and initialize a weather widget.
 * Created by dkluver on 10/6/17.
 */
public class SimpleWidgetExample implements WeatherWidget {

    private final double size;
    private final GraphicsGroup group;
    private final Image weatherIcon;

    public SimpleWidgetExample(double size) {
        this.size = size;

        group = new GraphicsGroup();

        weatherIcon = new Image(10, 10);
        weatherIcon.setMaxWidth(size - 20);
        weatherIcon.setMaxHeight(size - 20);
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
        weatherIcon.setImagePath(data.getCurrentConditions().getWeatherIcon());
        weatherIcon.setCenter(size * 0.5, size * 0.5);

        // Examples of how to get other weather data:
        System.out.println(data.getCityName());
        System.out.println(data.getCurrentConditions().getCloudCoverage());
        System.out.println(data.getCurrentConditions().getTemperature());
        System.out.println(data.getCurrentConditions().getPressure());
        System.out.println(data.getCurrentConditions().getHumidity());
        System.out.println(data.getCurrentConditions().getWindSpeed());
        System.out.println(data.getCurrentConditions().getWindDirectionAsString());
        System.out.println(data.getCurrentConditions().getSunriseTime());
        System.out.println(data.getCurrentConditions().getSunsetTime());
        System.out.println(data.getCurrentConditions().getWeatherDescription());
    }
}
