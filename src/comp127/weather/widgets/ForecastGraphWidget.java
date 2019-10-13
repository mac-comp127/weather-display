package comp127.weather.widgets;

import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import comp127graphics.*;

import java.awt.Color;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class ForecastGraphWidget implements WeatherWidget {
    private final double size;
    private GraphicsGroup group, iconGroup;
    private Polygon temperatureLine, temperatureRange;

    public ForecastGraphWidget(double size) {
        this.size = size;
        group = new GraphicsGroup();

        iconGroup = new GraphicsGroup();
        group.add(iconGroup);

        temperatureRange = new Polygon(new Point(0, 0));
        temperatureRange.setFillColor(new Color(0x9BCAFFF8, true));
        group.add(temperatureRange);
        temperatureRange.setStroked(false);

        temperatureLine = new Polygon(new Point(0, 0));
        temperatureLine.setStrokeColor(Color.BLACK);
        temperatureLine.setStrokeWidth(size / 100);
        group.add(temperatureLine);
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        // Find full range of all temperatures
        DoubleSummaryStatistics stats = data.getForecasts().stream()
            .flatMap((cast) -> Stream.of(
                cast.getTemperature(),
                cast.getMinTemperature(),
                cast.getMaxTemperature()))
            .filter(Objects::nonNull)
            .mapToDouble(Double::doubleValue)
            .summaryStatistics();
        double scale = 1 / (stats.getMax() - stats.getMin()),
               offset = stats.getMin();

        temperatureLine.setVertices(
            makeForecastPolygon(
                data.getForecasts(),
                ForecastConditions::getTemperature,
                ForecastConditions::getTemperature,
                scale, offset));

        temperatureRange.setVertices(
            makeForecastPolygon(
                data.getForecasts(),
                ForecastConditions::getMinTemperature,
                ForecastConditions::getMaxTemperature,
                scale, offset));

        iconGroup.removeAll();
        if (!data.getForecasts().isEmpty()) {
            Date startTime = data.getForecasts().get(0).getPredictionTime();
            double nextFreeX = Double.NEGATIVE_INFINITY;
            for (ForecastConditions forecast : data.getForecasts()) {
                if (forecast.getPredictionTime() == null) {
                    continue;
                }
                double x = timeToX(startTime, forecast.getPredictionTime());
                if (x < nextFreeX) {
                    continue;
                }
                Image icon = new Image(x, size * 0.3, forecast.getWeatherIcon());
                icon.setMaxWidth(Math.min(size / 3, Math.max(20, size / 15)));
                nextFreeX = icon.getX() + icon.getWidth() * 1.2;
                iconGroup.add(icon);
            }
        }
    }

    private List<Point> makeForecastPolygon(
            List<ForecastConditions> forecasts,
            Function<ForecastConditions,Double> metric0,
            Function<ForecastConditions,Double> metric1,
            double scale,
            double offset) {
        List<Point> list0 = makeForecastVertices(forecasts, metric0, scale, offset);
        List<Point> list1 = makeForecastVertices(forecasts, metric1, scale, offset);
        Collections.reverse(list1);
        list0.addAll(list1);

        if(list0.isEmpty()) {
            return List.of(new Point(0, 0));
        }
        return list0;
    }

    private List<Point> makeForecastVertices(
            List<ForecastConditions> forecasts,
            Function<ForecastConditions,Double> metric,
            double scale,
            double offset) {
        if (forecasts.isEmpty()) {
            return Collections.emptyList();
        }

        Date startTime = forecasts.get(0).getPredictionTime();
        if (startTime == null) {
            return Collections.emptyList();
        }

        List<Point> result = new ArrayList<>(forecasts.size());
        for(var forecast : forecasts) {
            Double temperature = metric.apply(forecast);
            Date time = forecast.getPredictionTime();
            if (temperature != null && time != null)
                result.add(
                    new Point(
                        timeToX(startTime, time),
                        size * 0.9 - size * 0.4 * (temperature - offset) * scale));
        }
        return result;
    }

    private double timeToX(Date startTime, Date time) {
        return size * (time.getTime() - startTime.getTime()) / (5 * 24*60*60*1000);
    }

    @Override
    public void onHover(Point position) {
    }
}
