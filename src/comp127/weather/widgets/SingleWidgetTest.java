package comp127.weather.widgets;

import comp127.weather.api.OpenWeatherProvider;
import comp127graphics.CanvasWindow;

public class SingleWidgetTest {
    public static void main(String[] args) {
        CanvasWindow canvas = new CanvasWindow("weather", 800, 800);
        WeatherWidget widget = new ForecastWidget();
        canvas.add(widget.getGraphics());

        OpenWeatherProvider conn = new OpenWeatherProvider("d6a22c9835563a57b372e6515fd8ec2b", 44.9, -93.0);
        conn.fetchWeather((data) -> {
            widget.update(data);
            canvas.draw();
        });
    }
}
