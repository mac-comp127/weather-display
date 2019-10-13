package comp127.weather.widgets;

import comp127.weather.ForecastBox;
import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ForecastWidget implements WeatherWidget {

    private final double size;
    private GraphicsGroup group;

    private GraphicsText dateLabel, timeLabel, tempLabel, minMaxTempLabel, description;
    private Image icon;
    private GraphicsGroup boxGroup;

    private List<ForecastBox> boxes = new ArrayList<>();

    public ForecastWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        dateLabel = new GraphicsText("", 0, 0);
        dateLabel.setFont(FontStyle.BOLD, size / 15);
        group.add(dateLabel);

        timeLabel = new GraphicsText("", 0, 0);
        timeLabel.setFont(FontStyle.BOLD, size / 15);
        group.add(timeLabel);

        icon = new Image(0, 0);
        icon.setMaxWidth(size / 4);
        icon.setMaxHeight(size / 4);
        group.add(icon);

        tempLabel = new GraphicsText("" + "\u2109", 0, 0);
        tempLabel.setFont(FontStyle.BOLD, size / 15);
        group.add(tempLabel);

        minMaxTempLabel = new GraphicsText("", 0, 0);
        minMaxTempLabel.setFont(FontStyle.PLAIN, size / 15);
        minMaxTempLabel.setFillColor(Color.GRAY);
        group.add(minMaxTempLabel);

        description = new GraphicsText("", 400, 120);
        description.setFont(FontStyle.PLAIN, size / 20);
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

        double margin = size / 30;
        double x = 0;  // position within parent comes from group
        double y = 0;
        double boxSpacing = size / 120;
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
        dateLabel.setText(FormattingHelpers.formatDate(forecast.getPredictionTime()));
        timeLabel.setText(FormattingHelpers.formatTimeOfDay(forecast.getPredictionTime()));
        icon.setImagePath(forecast.getWeatherIcon());
        tempLabel.setText(FormattingHelpers.formatDecimal(forecast.getTemperature()) + "\u2109");
        minMaxTempLabel.setText(
            FormattingHelpers.formatDecimal(forecast.getMinTemperature())
                + "\u2109 | "
                + FormattingHelpers.formatDecimal(forecast.getMaxTemperature())
                + "\u2109");
        description.setText(forecast.getWeatherDescription());
        updateLayout();
    }

    private void updateLayout() {
        dateLabel.setPosition(size / 24, size / 12);
        timeLabel.setPosition(size * 23 / 24 - timeLabel.getWidth(), size / 12);
        icon.setCenter(size / 2, size * 3.5 / 12);
        tempLabel.setCenter(size / 2, size * 6 / 12);

        minMaxTempLabel.setPosition(
            size / 2.0 - minMaxTempLabel.getWidth() / 2,
            tempLabel.getY() + minMaxTempLabel.getHeight() + 5);

        description.setPosition(
            size / 2.0 - description.getWidth() / 2,
            minMaxTempLabel.getY() + description.getHeight() + 5);

        boxGroup.setPosition(
            size / 2.0 - boxGroup.getWidth() / 2,
            size - boxGroup.getHeight() - size / 30);
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
