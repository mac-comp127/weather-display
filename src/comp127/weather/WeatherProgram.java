package comp127.weather;

import comp127.weather.api.OpenWeatherConnection;
import comp127.weather.api.WeatherData;
import comp127.weather.widgets.ForecastWidget;
import comp127.weather.widgets.TemperatureWidget;
import comp127.weather.widgets.WeatherWidget;
import comp127.weather.widgets.WindWidget;
import comp127graphics.CanvasWindow;

import java.awt.Color;
import java.util.List;

public class WeatherProgram extends CanvasWindow {

    //TODO: Replace the value of this string with your own API key.
    public static final String API_KEY = "d6a22c9835563a57b372e6515fd8ec2b";

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;

    //TODO: Define an instance variable that is an array of WeatherWidgets. You will also need a variable to keep track
    // of which index in the array is the widget that is currently displayed.
    private List<WeatherWidget> widgets;
    private int currentDisplayIndex = 0;

    public WeatherProgram() {
        super("Weather Display", WINDOW_WIDTH, WINDOW_HEIGHT);

        //TODO: Initialize your WeatherWidget array and add widgets to it. Add the first widget to the canvas so that it appears
        widgets = List.of(
            new TemperatureWidget(),
            new ForecastWidget(),
            new WindWidget()
        );

        add(widgets.get(0).getGraphics());

        OpenWeatherConnection conn = new OpenWeatherConnection(API_KEY, 44.9, -93.0);// saint paul
        WeatherData data = new WeatherData(conn);
        for (WeatherWidget widget : widgets) {
            widget.draw(data);
        }

        //TODO: Implement MouseMotion Listeners. When the mouse is moved you should:
        // 1. Check if your ForecastWidget is currently displayed.
        // 2. If so, you should update which forecast is displayed depending on which ForecastBox the mouse is currently hovering over.

        onMouseMove(event -> {
            if (widgets.get(currentDisplayIndex) instanceof ForecastWidget) {
                ((ForecastWidget) widgets.get(currentDisplayIndex)).updateSelection(event.getPosition());
            }
        });

        //TODO: Implement Mouse Listeners. When the mouse is clicked you should:
        // 1. remove everything from the canvas
        // 2. Advance your variable for which index is currently displayed. Keep in mind that it should cycle back to 0 when it gets to the end of the array.
        // 3. Add the new current widget to the canvas.

        onClick(event -> {
            currentDisplayIndex = (currentDisplayIndex + 1) % widgets.size();
            removeAll();
            add(widgets.get(currentDisplayIndex).getGraphics());
        });

        setBackground(new Color(153, 204, 255));
    }

    public static void main(String[] args) {
        new WeatherProgram();
    }
}
