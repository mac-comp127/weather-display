package comp127.weather.widgets;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

public class WindWidget implements WeatherWidget {

    private final double size, circleDiameter;

    private GraphicsGroup group;

    private GraphicsText windSpeedLabel;
    private GraphicsText windDescLabel;
    private Ellipse ring;
    private Line indicator;

    public WindWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        circleDiameter = size * 0.7;

        ring = new Ellipse(
            size / 2.0 - circleDiameter / 2,
            size / 2.0 - circleDiameter / 2,
            circleDiameter, circleDiameter);
        ring.setStrokeWidth(3);
        group.add(ring);

        indicator = new Line(0, 0, 0, 0);
        indicator.setStrokeWidth(3);
        group.add(indicator);

        GraphicsText northLabel = new GraphicsText("N", 0, 0);
        northLabel.setFont(FontStyle.PLAIN, size / 30);
        northLabel.setPosition(
            ring.getX() + circleDiameter / 2 - northLabel.getWidth() / 2,
            ring.getY() + circleDiameter / 6 - northLabel.getHeight() / 2);
        group.add(northLabel);

        windSpeedLabel = new GraphicsText("", 0, 0);
        windSpeedLabel.setFont(FontStyle.BOLD, size / 15);
        group.add(windSpeedLabel);

        windDescLabel = new GraphicsText("", 0, 0);
        windDescLabel.setFont(FontStyle.BOLD, size / 15);
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
        CurrentConditions currentConditions = data.getCurrentConditions();

        windSpeedLabel.setText(FormattingHelpers.formatDecimal(currentConditions.getWindSpeed()));
        if (currentConditions.getWindDirectionAsString() == null) {
            windDescLabel.setText("–");
        } else {
            windDescLabel.setText("Wind " + currentConditions.getWindDirectionAsString());
        }

        if(currentConditions.getWindDirectionInDegrees() == null) {
            indicator.setStartPosition(ring.getCenter());
            indicator.setEndPosition(ring.getCenter());
        } else {
            Point center = ring.getCenter();
            Point direction = Point.atAngle(
                Math.toRadians(
                    currentConditions.getWindDirectionInDegrees() - 90));
            double innerRadius = circleDiameter / 4.0;
            double outerRadius = 2.0 * circleDiameter / 4.0;
            indicator.setStartPosition(center.add(direction.scale(innerRadius)));
            indicator.setEndPosition(  center.add(direction.scale(outerRadius)));
        }

        updateLayout();
    }

    private void updateLayout() {
        windSpeedLabel.setCenter(ring.getCenter());
        windDescLabel.setPosition(
            ring.getX() + circleDiameter / 2 - windDescLabel.getWidth() / 2,
            ring.getY() + circleDiameter + size * 0.1);
    }
}
