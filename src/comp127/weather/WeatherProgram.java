package comp127.weather;

import comp127.weather.api.OpenWeatherProvider;
import comp127.weather.widgets.*;
import comp127graphics.CanvasWindow;
import comp127graphics.Rectangle;

import java.awt.Color;
import java.util.List;

/**
 * A weather UI that shows a collection of small widgets down one edge, and allows the user to
 * select a widget to enlarge.
 */
public class WeatherProgram {

    private static final double    // Location for which we're fetching weather
        FORECAST_LAT = 44.936593,  // OLRI 256 (approximate)
        FORECAST_LON = -93.168650;

    private CanvasWindow canvas;

    private double miniWidgetSize, largeWidgetSize;
    private List<WeatherWidget> miniWidgets, largeWidgets;
    private WeatherWidget displayedLargeWidget;
    private Rectangle selectionHighlight;

    /**
     * Opens a window, displays the weather UI, and fetches weather conditions.
     *
     * @param largeWidgetSize The height and width of the large widget. The window size is derived
     *      from this value combined with the number of widget choices.
     */
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

        updateWeather();
    }

    private void updateWeather() {
        new OpenWeatherProvider(FORECAST_LAT, FORECAST_LON)
            .fetchWeather((weatherData) -> {
                for (WeatherWidget widget : miniWidgets) {
                    widget.update(weatherData);
                }
                for (WeatherWidget widget : largeWidgets) {
                    widget.update(weatherData);
                }
                canvas.draw();
            });
    }

    private List<WeatherWidget> createWidgets(double size) {
        return List.of(
            new TemperatureWidget(size),
            new TemperatureWidget(size),  // TODO: Replace with your own widgets as you implement them
            new TemperatureWidget(size));
    }

    private void selectWidgetAtIndex(int index) {
        // TODO: Return without doing anything if index is out of bounds.
        //       (Hint: How do you know what the bounds are? Don’t assume it’s any specific number.
        //       There is an instance variable that you can use to figure out how many widgets
        //       there are.)

        // TODO: Remove the currently selected widget’s large UI from the canvas to make room for
        //       the new selection.
        //       (Hint: How do you get the currently selected large widget? Study the instance
        //              variables carefully.)
        //       (Hint: There might be no widget selected yet. How do you test for that?)
        //       (Hint: You can’t add or remove a WeatherWidget directly from the canvas, because a
        //              WeatherWidget is not a GraphicsObject. How do you get something that you
        //              _can_ add or remove? Study the WeatherWidget interface.)
        //       (Hint: The large widgets already have the correct size and position. You just need
        //              to add and remove them from the canvas.)

        // TODO: Update the currently displayed widget to be the one with the given index.
        //       (Think: why do we need to use an instance variable instead of a local variable?)

        // TODO: Add the newly selected widget’s UI to the canvas.

        // Hint on all of the TODOs: the description above is much longer than the actual code!
        // 👉 If you are writing a huge amount of code, you’re making it too complicated. 👈

        selectionHighlight.setPosition(largeWidgetSize, miniWidgetSize * index);
    }

    public static void main(String[] args) {
        new WeatherProgram(600);
    }
}
