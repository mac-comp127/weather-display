package comp127.weather.widgets;

import comp127.weather.WeatherProgram;
import comp127.weather.api.WeatherData;
import comp127graphics.GraphicsGroup;
import comp127graphics.GraphicsObject;
import comp127graphics.GraphicsText;
import comp127graphics.Image;

import java.awt.Font;
import java.text.DecimalFormat;

public class TemperatureWidget implements WeatherWidget {

    private GraphicsGroup group;

    private GraphicsText label;
    private GraphicsText description;
    private Image icon;

    private final DecimalFormat df = new DecimalFormat("#0.0");

    public TemperatureWidget() {
        group = new GraphicsGroup();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    public void update(WeatherData data) {
        icon = data.getWeatherIcon();
        group.add(icon, WeatherProgram.WINDOW_WIDTH / 2.0 - icon.getWidth() / 2, 0);

        Font tempFont = new Font("SanSerif", Font.BOLD, 60);
        label = new GraphicsText(df.format(data.getCurrentTemperature()) + "\u2109", 0, 0);
        label.setFont(tempFont);
        label.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - label.getWidth() / 2, icon.getHeight() + 30);

        group.add(label);

        Font descFont = new Font("SanSerif", Font.PLAIN, 30);
        description = new GraphicsText(data.getCurrentWeather(), 0, 0);
        description.setFont(descFont);
        description.setPosition(WeatherProgram.WINDOW_WIDTH / 2.0 - description.getWidth() / 2, icon.getHeight() + label.getHeight());

        group.add(description);
    }
}
