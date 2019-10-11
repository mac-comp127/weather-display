package comp127.weather.widgets;

import comp127.weather.ForecastBox;
import comp127.weather.WeatherProgram;
import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;

public class ForecastWidget implements WeatherWidget {

    private GraphicsGroup group;

    private GraphicsText timeLabel;
    private GraphicsText tempLabel;
    private GraphicsText minMaxTempLabel;
    private GraphicsText description;
    private Image icon;

    // This is used to format decimal numbers based on the number of digits you want to display
    // e.g. System.out.println(df.format(4.555555)); will print 4.5;
    private final DecimalFormat df = new DecimalFormat("#0.0");

    public ForecastWidget() {
        group = new GraphicsGroup();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    public void update(WeatherData data) {
        //TODO: Draw a ForecastBox for each forecastwrapper in the array returned from getForecastArray().

        //TODO: Draw information about the forecast at the first array index.
        makeLabels(data);
        drawForecastBoxes(data);
    }

    private void makeLabels(WeatherData data) {
        ForecastConditions firstForecast = data.getForecasts().get(0);

        Font timeFont = new Font("SanSerif", Font.BOLD, 40);
        timeLabel = new GraphicsText(firstForecast.getPredictionTime().toString(), 0, 0);
        timeLabel.setFont(timeFont);
        timeLabel.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - timeLabel.getWidth() / 2, 50);
        group.add(timeLabel);

        icon = firstForecast.getWeatherIcon();
        group.add(icon, WeatherProgram.WINDOW_WIDTH / 2.0 - icon.getWidth() / 2, timeLabel.getHeight());

        Font tempFont = new Font("SanSerif", Font.BOLD, 40);
        tempLabel = new GraphicsText(df.format(firstForecast.getPredictedTemperature()) + "\u2109", 0, 0);
        tempLabel.setFont(tempFont);
        tempLabel.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - tempLabel.getWidth() / 2, icon.getHeight());
        group.add(tempLabel);

        Font minMaxFont = new Font("SanSerif", Font.PLAIN, 40);
        minMaxTempLabel = new GraphicsText(df.format(firstForecast.getPredictedMinTemperature()) + "\u2109 | " + df.format(firstForecast.getPredictedMaxTemperature()) + "\u2109", 0, 0);
        minMaxTempLabel.setFont(minMaxFont);
        minMaxTempLabel.setFillColor(Color.GRAY);
        minMaxTempLabel.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - minMaxTempLabel.getWidth() / 2, icon.getHeight() + tempLabel.getHeight());
        group.add(minMaxTempLabel);

        Font descFont = new Font("SanSerif", Font.PLAIN, 30);
        description = new GraphicsText(firstForecast.getPredictedWeather(), 400, 120);
        description.setFont(descFont);
        description.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - description.getWidth() / 2, icon.getHeight() + tempLabel.getHeight() + minMaxTempLabel.getHeight());

        group.add(description);
    }

    private void drawForecastBoxes(WeatherData data) {
        List<ForecastConditions> forecasts = data.getForecasts();

        double y = icon.getHeight() + tempLabel.getHeight() + minMaxTempLabel.getHeight() + description.getHeight() + 30;
        double x = 20;
        final double BOX_SPACING = 15;
        final double BOX_WIDTH = 20;
        final double BOX_HEIGHT = 30;
        for (ForecastConditions forecast : forecasts) {
            x += BOX_WIDTH + BOX_SPACING;
            if (x + BOX_WIDTH + BOX_SPACING > WeatherProgram.WINDOW_WIDTH) {
                x = 20;
                y += BOX_HEIGHT + BOX_SPACING * 2;
            }

            ForecastBox box = new ForecastBox(forecast, x, y, BOX_WIDTH, BOX_HEIGHT);
            group.add(box);
        }
    }

    /**
     * Given x,y coordinates, this returns the forecastbox at that position if it exists
     * @param location pos to check
     * @return null if not over a forecast box
     */
    private ForecastBox getBoxAt(Point location) {
        GraphicsObject obj = group.getElementAt(location);
        if (obj instanceof ForecastBox) {
            return (ForecastBox) obj;
        }
        return null;
    }

    /**
     * Updates the currently displayed forecast information depending on which ForecastBox is located at x,y.
     * If there is not a ForecastBox at that position the display does not change.
     */
    public void updateSelection(Point location) {
        ForecastBox box = getBoxAt(location);
        if (box != null) {
            //TODO: Update the current displayed information to match the selected forecast from the box.
            ForecastConditions forecast = box.getForecast();
            timeLabel.setText(forecast.getPredictionTime().toString());
            timeLabel.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - timeLabel.getWidth() / 2, 50);
            group.remove(icon);
            icon = forecast.getWeatherIcon();
            group.add(icon, WeatherProgram.WINDOW_WIDTH / 2.0 - icon.getWidth() / 2, timeLabel.getHeight());
            tempLabel.setText(df.format(forecast.getPredictedTemperature()) + "\u2109");
            tempLabel.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - tempLabel.getWidth() / 2, icon.getHeight());
            minMaxTempLabel.setText(df.format(forecast.getPredictedMinTemperature()) + "\u2109 | " + df.format(forecast.getPredictedMaxTemperature()) + "\u2109");
            minMaxTempLabel.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - minMaxTempLabel.getWidth() / 2, icon.getHeight() + tempLabel.getHeight());
            description.setText(forecast.getPredictedWeather());
            description.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - description.getWidth() / 2, icon.getHeight() + tempLabel.getHeight() + minMaxTempLabel.getHeight());

        }
    }

}
