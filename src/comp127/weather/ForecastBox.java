package comp127.weather;

import comp127.weather.api.Forecast;
import comp127graphics.Rectangle;

import java.awt.Color;


/**
 * A ForecastBox is a rectangle that represents a specific forecast.
 */
public class ForecastBox extends Rectangle {

    // This holds the information about a specific forecast
    private Forecast forecast;


    public ForecastBox(Forecast forecast, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.forecast = forecast;

        setFilled(true);
        setFillColor(new Color(59, 166, 52));
    }

    /**
     * Getter for forcast object
     * @return
     */
    public Forecast getForecast() {
        return forecast;
    }
}
