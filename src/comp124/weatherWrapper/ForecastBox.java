package comp124.weatherWrapper;

import comp124graphics.Rectangle;
import java.awt.*;


/**
 * A ForecastBox is a rectangle that represents a specific forecast.
 */
public class ForecastBox extends Rectangle {

    // This holds the information about a specific forecast
    private ForecastWrapper forecast;


    public ForecastBox(ForecastWrapper forecast, double x, double y, double width, double height){
        super(x, y, width, height);
        this.forecast = forecast;

        setFilled(true);
        setFillColor(new Color(59, 166, 52));
    }

    /**
     * Getter for forcast object
     * @return
     */
    public ForecastWrapper getForecast() {
        return forecast;
    }
}
