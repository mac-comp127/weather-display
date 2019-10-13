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

    public TemperatureWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        icon = new Image(0, 0);  // we'll position it when we have an icon
        icon.setMaxWidth(size);
        icon.setMaxHeight(size * 0.5);
        group.add(icon);

        label = new GraphicsText("–", 0, 0);
        label.setFont(FontStyle.BOLD, size * 0.1);
        group.add(label);

        description = new GraphicsText("–", 0, 0);
        description.setFont(FontStyle.PLAIN, size * 0.05);
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
        label.setText(FormattingHelpers.formatDecimal(currentConditions.getTemperature()) + "\u2109");
        description.setText(currentConditions.getWeatherDescription());

        updateLayout();
    }

    private void updateLayout() {
        icon.setCenter(size * 0.5, size * 0.4);

        label.setCenter(size * 0.5, size * 0.8);

        description.setPosition(
            size * 0.5 - description.getWidth() / 2,
            label.getY() + description.getHeight());
    }
}
