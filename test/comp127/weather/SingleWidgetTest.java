package comp127.weather;

import comp127.weather.api.WeatherData;
import comp127.weather.api.WeatherDataFixtures;
import comp127.weather.widgets.SimpleWidgetExample;
import comp127.weather.widgets.WeatherWidget;
import comp127graphics.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Create multiple instances of a single widget at a variety of different sizes, and feeds the
 * instances varied test conditions.
 */
public class SingleWidgetTest {

    private static WeatherWidget makeWidget(Point size) {
        // Replace SimpleWidgetExample with your own widget to test
        //         ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇
        return new SimpleWidgetExample(size);
    }

    // –––––– Test code ––––––

    private static final List<WeatherData> testData = List.of();

    private final GraphicsGroup widgetGroup = new GraphicsGroup();
    private final GraphicsGroup borderGroup = new GraphicsGroup();
    private final List<WeatherWidget> widgets = new ArrayList<>();
    private final GraphicsText seedLabel;
    private int testDataSeed = 0;

    private SingleWidgetTest() {
        CanvasWindow canvas = new CanvasWindow("Weather widget test", 825, 600);
        canvas.add(borderGroup);
        canvas.add(widgetGroup);

        addTestWidget(0, 0, 400, 400);
        addTestWidget(400, 0, 300, 300);
        addTestWidget(400, 300, 100, 100);
        addTestWidget(500, 300, 200, 250);
        addTestWidget(700, 0, 125, 550);
        addTestWidget(0, 400, 500, 150);

        seedLabel = new GraphicsText("Showing state before update() is called", 10, 580);
        canvas.add(seedLabel);

        GraphicsText helpText = new GraphicsText("Click anywhere to generate new test data", 0, 580);
        helpText.setX(canvas.getWidth() - helpText.getWidth() - 10);
        helpText.setFillColor(Color.BLUE);
        canvas.add(helpText);

        canvas.onClick((event) -> {
            seedLabel.setText("Showing testDataSeed = " + testDataSeed
                + (testDataSeed == 0 ? " (state when data is missing from API)" : ""));
            WeatherData data = WeatherDataFixtures.generateWeatherData(testDataSeed);
            System.out.println(data);
            for (WeatherWidget widget : widgets) {
                widget.update(data);
            }
            testDataSeed++;
        });
    }

    private void addTestWidget(double x, double y, double w, double h) {
        WeatherWidget widget = makeWidget(new Point(w, h));
        widgetGroup.add(widget.getGraphics(), x, y);
        widgets.add(widget);

        Rectangle border = new Rectangle(x, y, w, h);
        border.setStrokeColor(new Color(0x40000000, true));
        border.setStrokeWidth(4);
        borderGroup.add(border);
        borderGroup.add(border);
    }

    public static void main(String[] args) {
        new SingleWidgetTest();
    }
}
