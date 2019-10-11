package comp127.weather.widgets;

import comp127.weather.api.OpenWeatherConnection;
import comp127.weather.api.WeatherData;
import comp127graphics.CanvasWindow;

/**
 * This example shows how to create a weather connection and initialize a weather widget.
 * Created by dkluver on 10/6/17.
 */
public class SimpleWidgetExample extends WeatherWidget {

    public SimpleWidgetExample() {
    }

    public void draw(WeatherData data) {
        add(data.getWeatherIcon());
    }

    public static void main(String[] args) {
        CanvasWindow canvas = new CanvasWindow("weather", 800, 600);
        SimpleWidgetExample widget = new SimpleWidgetExample();
        canvas.add(widget);

        OpenWeatherConnection conn = new OpenWeatherConnection("d6a22c9835563a57b372e6515fd8ec2b", 44.9, -93.0);
        WeatherData data = new WeatherData(conn);
        widget.draw(data);

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
