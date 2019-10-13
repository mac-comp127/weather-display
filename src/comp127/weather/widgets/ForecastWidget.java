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
import java.util.ArrayList;
import java.util.List;

public class ForecastWidget implements WeatherWidget {

    private final double size;
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
    private List<ForecastBox> boxes = new ArrayList<>();

    public ForecastWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        timeLabel = new GraphicsText("–", 0, 0);
        timeLabel.setFont(new Font("SanSerif", Font.BOLD, (int) Math.round(size / 15)));
        group.add(timeLabel);

        icon = new Image(0, 0);
        icon.setMaxWidth(size / 3);
        icon.setMaxHeight(size / 3);
        group.add(icon);

        tempLabel = new GraphicsText("–" + "\u2109", 0, 0);
        tempLabel.setFont(new Font("SanSerif", Font.BOLD, (int) Math.round(size / 15)));
        group.add(tempLabel);

        minMaxTempLabel = new GraphicsText("–", 0, 0);
        minMaxTempLabel.setFont(new Font("SanSerif", Font.PLAIN, (int) Math.round(size / 15)));
        minMaxTempLabel.setFillColor(Color.GRAY);
        group.add(minMaxTempLabel);

        description = new GraphicsText("–", 400, 120);
        description.setFont(new Font("SanSerif", Font.PLAIN, (int) Math.round(size / 20)));
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

        double margin = size / 30;
        double x = 0;  // position within parent comes from group
        double y = 0;
        double boxSpacing = size / 60;
        double boxWidth = size / 30;
        double boxHeight = size / 20;

        boxGroup.removeAll();
        boxes.clear();
        for (ForecastConditions forecast : data.getForecasts()) {
            ForecastBox box = new ForecastBox(forecast, x, y, boxWidth, boxHeight);
            boxGroup.add(box);
            boxes.add(box);

            x += boxWidth + boxSpacing;
            if (x + boxWidth > size - margin * 2) {
                x = 0;
                y += boxHeight + boxSpacing * 2;
            }
        }
        if (!boxes.isEmpty()) {
            selectForecast(boxes.get(0));
        }
    }

    private void selectForecast(ForecastBox box) {
        for (ForecastBox otherBox : boxes) {
            otherBox.setActive(otherBox == box);
        }
        ForecastConditions forecast = box.getForecast();
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
            size / 2.0 - timeLabel.getWidth() / 2,
            size / 12);

        icon.setPosition(
            size / 2.0 - icon.getWidth() / 2,
            timeLabel.getHeight());

        tempLabel.setPosition(
            size / 2.0 - tempLabel.getWidth() / 2,
            icon.getBounds().getMaxY());

        minMaxTempLabel.setPosition(
            size / 2.0 - minMaxTempLabel.getWidth() / 2,
            tempLabel.getY() + minMaxTempLabel.getHeight() + 5);

        description.setPosition(
            size / 2.0 - description.getWidth() / 2,
            minMaxTempLabel.getY() + description.getHeight() + 5);

        boxGroup.setPosition(
            size / 2.0 - boxGroup.getWidth() / 2,
            size * 0.9 - boxGroup.getHeight());
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
            selectForecast(box);
        }
    }
}
