package comp127.weather.widgets;

import comp127.weather.WeatherProgram;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Font;
import java.text.DecimalFormat;

public class TemperatureWidget implements WeatherWidget {

    private GraphicsGroup group;

    private GraphicsText label;
    private GraphicsText description;
    private Image icon;

    private final DecimalFormat oneDecimalPlace = new DecimalFormat("#0.0");

    public TemperatureWidget() {
        group = new GraphicsGroup();

        icon = new Image(0, 0);  // we'll position it when we have an icon
        group.add(icon);

        label = new GraphicsText("–", 0, 0);
        label.setFont(new Font("SanSerif", Font.BOLD, 60));
        group.add(label);

        Font descFont = new Font("SanSerif", Font.PLAIN, 30);
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
        icon.setImagePath(data.getWeatherIcon());
        label.setText(oneDecimalPlace.format(data.getCurrentTemperature()) + "\u2109");
        description.setText(data.getCurrentWeather());

        updateLayout();
    }

    private void updateLayout() {
        icon.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - icon.getWidth() / 2,
            30);

        label.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - label.getWidth() / 2,
            icon.getBounds().getMaxY() + 30);

        description.setPosition(
            WeatherProgram.WINDOW_WIDTH / 2.0 - description.getWidth() / 2,
            label.getY() + description.getHeight());
    }
}
