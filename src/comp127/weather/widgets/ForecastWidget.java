package comp127.weather.widgets;

import comp127.weather.ForecastBox;
import comp127.weather.WeatherProgram;
import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastWidget implements WeatherWidget {

    private GraphicsGroup group;

    private GraphicsText timeLabel;
    private GraphicsText tempLabel;
    private GraphicsText minMaxTempLabel;
    private GraphicsText description;
    private Image icon;
    private GraphicsGroup boxGroup;

    // This is used to format decimal numbers based on the number of digits you want to display
    // e.g. System.out.println(df.format(4.555555)); will print 4.5;
    private final DecimalFormat oneDecimalPlace = new DecimalFormat("#0.0");
    private final DateFormat dateFormat = new SimpleDateFormat("E, MMM d – h:mm a");

    public ForecastWidget() {
        group = new GraphicsGroup();

        Font timeFont = new Font("SanSerif", Font.BOLD, 40);
        timeLabel = new GraphicsText("–", 0, 0);
        timeLabel.setFont(timeFont);
        group.add(timeLabel);

        icon = new Image(0, 0);
        group.add(icon);

        Font tempFont = new Font("SanSerif", Font.BOLD, 40);
        tempLabel = new GraphicsText("–" + "\u2109", 0, 0);
        tempLabel.setFont(tempFont);
        group.add(tempLabel);

        Font minMaxFont = new Font("SanSerif", Font.PLAIN, 40);
        minMaxTempLabel = new GraphicsText("–", 0, 0);
        minMaxTempLabel.setFont(minMaxFont);
        minMaxTempLabel.setFillColor(Color.GRAY);
        group.add(minMaxTempLabel);

        Font descFont = new Font("SanSerif", Font.PLAIN, 30);
        description = new GraphicsText("–", 400, 120);
        description.setFont(descFont);
        group.add(description);

        boxGroup = new GraphicsGroup();
        group.add(boxGroup);

        updateLayout();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    public void update(WeatherData data) {
        //TODO: Draw a ForecastBox for each forecastwrapper in the array returned from getForecastArray().

        //TODO: Draw information about the forecast at the first array index.

        List<ForecastConditions> forecasts = data.getForecasts();

        double y = icon.getHeight() + tempLabel.getHeight() + minMaxTempLabel.getHeight() + description.getHeight() + 30;
        double x = 20;
        final double BOX_SPACING = 15;
        final double BOX_WIDTH = 20;
        final double BOX_HEIGHT = 30;

        boxGroup.removeAll();
        for (ForecastConditions forecast : forecasts) {
            ForecastBox box = new ForecastBox(forecast, x, y, BOX_WIDTH, BOX_HEIGHT);
            boxGroup.add(box);

            x += BOX_WIDTH + BOX_SPACING;
            if (x + BOX_WIDTH + BOX_SPACING > WeatherProgram.WINDOW_WIDTH) {
                x = 20;
                y += BOX_HEIGHT + BOX_SPACING * 2;
            }
        }
        if (!forecasts.isEmpty()) {
            showConditions(forecasts.get(0));
        }
    }

    private void showConditions(ForecastConditions forecast) {
        timeLabel.setText(dateFormat.format(forecast.getPredictionTime()));
        icon.setImagePath(forecast.getWeatherIcon());
        tempLabel.setText(oneDecimalPlace.format(forecast.getTemperature()) + "\u2109");
        minMaxTempLabel.setText(
            oneDecimalPlace.format(forecast.getMinTemperature())
                + "\u2109 | "
                + oneDecimalPlace.format(forecast.getMaxTemperature())
                + "\u2109");
        description.setText(forecast.getWeatherDescription());
        updateLayout();
    }

    private void updateLayout() {
        timeLabel.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - timeLabel.getWidth() / 2,
            50);

        icon.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - icon.getWidth() / 2,
            timeLabel.getHeight());

        tempLabel.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - tempLabel.getWidth() / 2,
            icon.getBounds().getMaxY());

        minMaxTempLabel.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - minMaxTempLabel.getWidth() / 2,
            tempLabel.getY() + minMaxTempLabel.getHeight() + 5);

        description.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - description.getWidth() / 2,
            minMaxTempLabel.getY() + description.getHeight() + 5);

        boxGroup.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - boxGroup.getWidth() / 2,
            WeatherProgram.WINDOW_HEIGHT - boxGroup.getHeight() - 30);
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
    @Override
    public void onHover(Point position) {
        ForecastBox box = getBoxAt(position);
        if (box != null) {
            //TODO: Update the current displayed information to match the selected forecast from the box.
            showConditions(box.getForecast());
        }
    }
}
