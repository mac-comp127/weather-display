package comp127.weather.widgets;

import comp127.weather.WeatherProgram;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Font;
import java.text.DecimalFormat;

public class WindWidget implements WeatherWidget {

    private static final double ELLIPSE_WIDTH = 300;
    private static final double ELLIPSE_HEIGHT = 300;

    private GraphicsGroup group;

    private GraphicsText windSpeedLabel;
    private GraphicsText windDescLabel;
    private Ellipse ring;
    private Line indicator;

    private final DecimalFormat oneDecimalPlace = new DecimalFormat("#0.0");

    public WindWidget() {
        group = new GraphicsGroup();

        ring = new Ellipse(
            WeatherProgram.WINDOW_WIDTH / 2.0 - ELLIPSE_WIDTH / 2,
            WeatherProgram.WINDOW_HEIGHT / 2.0 - ELLIPSE_HEIGHT / 2,
            ELLIPSE_WIDTH, ELLIPSE_HEIGHT);
        ring.setStrokeWidth(3);
        group.add(ring);

        indicator = new Line(0, 0, 0, 0);
        indicator.setStrokeWidth(3);
        group.add(indicator);

        Font dirFont = new Font("SanSerif", Font.PLAIN, 20);
        GraphicsText northLabel = new GraphicsText("N", 0, 0);
        northLabel.setFont(dirFont);
        northLabel.setPosition(
            ring.getX() + ELLIPSE_WIDTH / 2 - northLabel.getWidth() / 2,
            ring.getY() + ELLIPSE_HEIGHT / 6 - northLabel.getHeight() / 2);
        group.add(northLabel);

        Font windFont = new Font("SanSerif", Font.BOLD, 40);
        windSpeedLabel = new GraphicsText("–", 0, 0);
        windSpeedLabel.setFont(windFont);
        group.add(windSpeedLabel);

        windDescLabel = new GraphicsText("–", 0, 0);
        windDescLabel.setFont(windFont);
        group.add(windDescLabel);

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
        windSpeedLabel.setText(oneDecimalPlace.format(data.getCurrentWindSpeed()));
        windDescLabel.setText("Wind " + data.getCurrentWindDirection());

        Point center = ring.getCenter();
        Point direction = Point.atAngle(
            Math.toRadians(
                data.getCurrentWindDegree()));
        double innerRadius = ELLIPSE_WIDTH / 4.0;
        double outerRadius = 2.0 * ELLIPSE_WIDTH / 4.0;
        indicator.setStartPosition(center.add(direction.scale(innerRadius)));
        indicator.setEndPosition(  center.add(direction.scale(outerRadius)));

        updateLayout();
    }

    private void updateLayout() {
        windSpeedLabel.setPosition(
            ring.getX() + ELLIPSE_WIDTH / 2 - windSpeedLabel.getWidth() / 2,
            ring.getY() + ELLIPSE_HEIGHT / 2 + windSpeedLabel.getHeight() / 4);
        windDescLabel.setPosition(
            ring.getX() + ELLIPSE_WIDTH / 2 - windDescLabel.getWidth() / 2,
            ring.getY() + ELLIPSE_HEIGHT + 60);
    }
}
