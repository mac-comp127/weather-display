package comp127.weather;

import comp127.weather.api.OpenWeatherProvider;
import comp127.weather.widgets.*;
import comp127graphics.CanvasWindow;
import comp127graphics.Rectangle;

import java.awt.Color;
import java.util.List;

public class WeatherProgram {

    //TODO: Replace the value of this string with your own API key.
    private static final String API_KEY = "d6a22c9835563a57b372e6515fd8ec2b";

    private static final double     // Location for which we're fetching weather
        FORECAST_LAT = 44.936593,  // OLRI 256 (approximate)
        FORECAST_LON = -93.168650;

    //TODO: Define an instance variable that is an array of WeatherWidgets. You will also need a variable to keep track
    // of which index in the array is the widget that is currently displayed.
    private CanvasWindow canvas;
    double miniWidgetSize, largeWidgetSize;
    private List<WeatherWidget> miniWidgets, largeWidgets;
    private WeatherWidget displayedLargeWidget;
    private Rectangle selectionHighlight;
    private int currentDisplayIndex = 0;

    public WeatherProgram(double largeWidgetSize) {
        this.largeWidgetSize = largeWidgetSize;
        largeWidgets = createWidgets(largeWidgetSize);

        miniWidgetSize = largeWidgetSize / largeWidgets.size();  // so they stack along one edge
        miniWidgets = createWidgets(miniWidgetSize);

        canvas = new CanvasWindow(
            "Weather Display",
            (int) Math.round(largeWidgetSize + miniWidgetSize),
            (int) Math.round(largeWidgetSize));
        canvas.setBackground(new Color(153, 204, 255));

        selectionHighlight = new Rectangle(0, 0, miniWidgetSize, miniWidgetSize);  // selectWidgetAtIndex() will position it
        selectionHighlight.setStroked(false);
        selectionHighlight.setFillColor(new Color(0x7FFFFFFF, true));
        canvas.add(selectionHighlight);

        double y = 0;
        for (WeatherWidget widget : miniWidgets) {
            canvas.add(widget.getGraphics(), largeWidgetSize, y);
            y += miniWidgetSize;
        }
        selectWidgetAtIndex(0);

        new OpenWeatherProvider(API_KEY, FORECAST_LAT, FORECAST_LON)
            .fetchWeather((weatherData) -> {
                for (WeatherWidget widget : miniWidgets) {
                    widget.update(weatherData);
                }
                for (WeatherWidget widget : largeWidgets) {
                    widget.update(weatherData);
                }
                canvas.draw();
            });

        canvas.onMouseMove(event -> {
            if (displayedLargeWidget != null && event.getPosition().getX() < largeWidgetSize) {
                displayedLargeWidget.onHover(event.getPosition());
            }
        });

        canvas.onClick(event -> {
            if (event.getPosition().getX() >= largeWidgetSize) {
                selectWidgetAtIndex(
                    (int) (event.getPosition().getY() / largeWidgetSize * miniWidgets.size()));
            }
        });
    }

    private List<WeatherWidget> createWidgets(double size) {
        return List.of(
            new TemperatureWidget(size),
            new WindWidget(size),
            new ForecastWidget(size),
            new ForecastGraphWidget(size));
    }

    private void selectWidgetAtIndex(int index) {
        if (index < 0 || index >= largeWidgets.size()) {
            return;
        }

        if (displayedLargeWidget != null) {
            canvas.remove(displayedLargeWidget.getGraphics());
        }
        displayedLargeWidget = largeWidgets.get(index);
        canvas.add(displayedLargeWidget.getGraphics());

        selectionHighlight.setPosition(largeWidgetSize, miniWidgetSize * index);
    }

    public static void main(String[] args) {
        new WeatherProgram(600);
    }
}
