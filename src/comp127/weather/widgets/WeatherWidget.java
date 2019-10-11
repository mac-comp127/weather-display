package comp127.weather.widgets;

import comp127.weather.api.WeatherData;
import comp127graphics.GraphicsGroup;

/**
 * A parent class for widgets based on the open weather API.
 */
public abstract class WeatherWidget extends GraphicsGroup {

    /**
     * Draws the graphical representation of the widget
     */
    public abstract void draw(WeatherData data);
}
