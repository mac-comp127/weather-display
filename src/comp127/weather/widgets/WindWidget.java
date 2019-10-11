package comp127.weather.widgets;

import comp127.weather.api.OpenWeatherConnection;
import comp127.weather.WeatherProgram;
import comp127graphics.Ellipse;
import comp127graphics.GraphicsText;
import comp127graphics.Line;

import java.awt.Font;
import java.text.DecimalFormat;

public class WindWidget extends WeatherWidget {

    public static final double ELLIPSE_WIDTH = 300;
    public static final double ELLIPSE_HEIGHT = 300;

    private GraphicsText windSpeedLabel;
    private GraphicsText windDescLabel;

    private final DecimalFormat df = new DecimalFormat("#0.0");

    public WindWidget(OpenWeatherConnection conn) {
        super(conn);
        draw();
    }

    protected void draw() {
        Ellipse circle = new Ellipse(WeatherProgram.WINDOW_WIDTH / 2 - ELLIPSE_WIDTH / 2, WeatherProgram.WINDOW_HEIGHT / 2 - ELLIPSE_HEIGHT / 2, ELLIPSE_WIDTH, ELLIPSE_HEIGHT);
        circle.setStrokeWidth(3);
        add(circle);

        Font dirFont = new Font("SanSerif", Font.PLAIN, 20);
        GraphicsText northLabel = new GraphicsText("N", 0, 0);
        northLabel.setFont(dirFont);
        northLabel.setPosition(circle.getX() + ELLIPSE_WIDTH / 2 - northLabel.getWidth() / 2, circle.getY() + ELLIPSE_HEIGHT / 6 - northLabel.getHeight() / 2);
        add(northLabel);

        Font windFont = new Font("SanSerif", Font.BOLD, 40);
        windSpeedLabel = new GraphicsText(df.format(getCurrentWindSpeed()), 0, 0);
        windSpeedLabel.setFont(windFont);
        windSpeedLabel.setPosition(circle.getX() + ELLIPSE_WIDTH / 2 - windSpeedLabel.getWidth() / 2, circle.getY() + ELLIPSE_HEIGHT / 2 + windSpeedLabel.getHeight() / 4);
        add(windSpeedLabel);

        windDescLabel = new GraphicsText("Wind " + getCurrentWindDirection(), 0, 0);
        windDescLabel.setFont(windFont);
        windDescLabel.setPosition(circle.getX() + ELLIPSE_WIDTH / 2 - windDescLabel.getWidth() / 2, circle.getY() + ELLIPSE_HEIGHT + 60);
        add(windDescLabel);

        double degreesOffNorth = getCurrentWindDegree();
        double radiansOffNorth = Math.toRadians(degreesOffNorth);
        double centerX = circle.getX() + ELLIPSE_WIDTH / 2.0;
        double centerY = circle.getY() + ELLIPSE_HEIGHT / 2.0;
        double innerRadius = ELLIPSE_WIDTH / 4.0;
        double outerRadius = 2.0 * ELLIPSE_WIDTH / 4.0;
        double x1 = centerX + innerRadius * Math.sin(radiansOffNorth);
        double y1 = centerY + innerRadius * -Math.cos(radiansOffNorth);
        double x2 = centerX + outerRadius * Math.sin(radiansOffNorth);
        double y2 = centerY + outerRadius * -Math.cos(radiansOffNorth);
        Line indicator = new Line(x1, y1, x2, y2);
        indicator.setStrokeWidth(3);
        add(indicator);

    }
}
