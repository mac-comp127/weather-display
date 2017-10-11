package comp124.weatherWrapper;

import comp124graphics.CanvasWindow;

/**
 * This example shows how to create a weather connection and initialize a weather widget.
 * Created by dkluver on 10/6/17.
 */
public class SimpleWidgetExample extends WeatherWidget {
    public SimpleWidgetExample(OpenWeatherConnection connection) {
        super(connection);
        draw();
    }

    protected void draw(){
        add(getWeatherIcon());
    }

    public static void main(String[] args) {
        OpenWeatherConnection conn = new OpenWeatherConnection("d6a22c9835563a57b372e6515fd8ec2b", -44.9, 93.0);
        // saint paul

        CanvasWindow cw = new CanvasWindow("weather", 800, 600);
        SimpleWidgetExample sw = new SimpleWidgetExample(conn);
        cw.add(sw);

        System.out.println(sw.getCurrentCloudCoverage());
        System.out.println(sw.getCurrentTemperature());
        System.out.println(sw.getCurrentPressure());
        System.out.println(sw.getCurrentHumidity());
        System.out.println(sw.getCurrentWindSpeed());
        System.out.println(sw.getCurrentWindDirection());
        System.out.println(sw.getSunrise());
        System.out.println(sw.getSunset());
        System.out.println(sw.getCurrentWeather());
    }
}
