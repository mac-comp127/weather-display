package comp127.weather.widgets;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Font;
import java.text.DecimalFormat;

public class TemperatureWidget implements WeatherWidget {

    private final double size;
    private GraphicsGroup group;

    private GraphicsText label;
    private GraphicsText description;
    private Image icon;

    private final DecimalFormat oneDecimalPlace = new DecimalFormat("#0.0");

    public TemperatureWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        icon = new Image(0, 0);  // we'll position it when we have an icon
        icon.setMaxWidth(size);
        icon.setMaxHeight(size * 0.7);
        group.add(icon);

        label = new GraphicsText("–", 0, 0);
        label.setFont(new Font("SanSerif", Font.BOLD, (int) Math.round(size * 0.1)));
        group.add(label);

        Font descFont = new Font("SanSerif", Font.PLAIN, (int) Math.round(size * 0.05));
        description = new GraphicsText("–", 0, 0);
        description.setFont(descFont);
        group.add(description);

        updateLayout();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void onHover(Point position) {
    }

    public void update(WeatherData data) {
        CurrentConditions currentConditions = data.getCurrentConditions();

        icon.setImagePath(currentConditions.getWeatherIcon());
        label.setText(oneDecimalPlace.format(currentConditions.getTemperature()) + "\u2109");
        description.setText(currentConditions.getWeatherDescription());

        updateLayout();
    }

    private void updateLayout() {
        double topMargin = size * 0.05;

        icon.setPosition(
            size / 2.0 - icon.getWidth() / 2,
            topMargin);

        label.setPosition(
            size / 2.0 - label.getWidth() / 2,
            icon.getBounds().getMaxY() + topMargin);

        description.setPosition(
            size / 2.0 - description.getWidth() / 2,
            label.getY() + description.getHeight());
    }
}
