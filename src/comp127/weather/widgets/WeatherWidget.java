package comp127.weather.widgets;

import comp127.weather.api.WeatherData;
import comp127graphics.GraphicsObject;
import comp127graphics.Point;

/**
 * A parent class for widgets based on the open weather API.
 */
public interface WeatherWidget {

    GraphicsObject getGraphics();

    /**
     * Draws the graphical representation of the widget
     */
    void update(WeatherData data);

    void onHover(Point position);
}
