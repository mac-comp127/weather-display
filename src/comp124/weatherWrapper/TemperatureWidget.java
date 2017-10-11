package comp124.weatherWrapper;

import comp124graphics.GraphicsText;
import comp124graphics.Image;

import java.awt.*;
import java.text.DecimalFormat;

public class TemperatureWidget extends WeatherWidget {

    private GraphicsText tempLabel;
    private GraphicsText description;
    private Image icon;

    private final DecimalFormat df = new DecimalFormat("#0.0");

    public TemperatureWidget(OpenWeatherConnection conn){
        super(conn);
        draw();
    }

    protected void draw(){
        icon = getWeatherIcon();
        add(icon, WeatherProgram.WINDOW_WIDTH/2-icon.getWidth()/2, 0);
        
        
        Font tempFont = new Font("SanSerif", Font.BOLD, 60);
        tempLabel = new GraphicsText(df.format(getCurrentTemperature())+"\u2109", 0, 0);
        tempLabel.setFont(tempFont);
        tempLabel.setPosition(WeatherProgram.WINDOW_WIDTH/2 - tempLabel.getWidth()/2, icon.getHeight() + 30);

        add(tempLabel);

        Font descFont = new Font("SanSerif", Font.PLAIN, 30);
        description = new GraphicsText(getCurrentWeather(), 0, 0);
        description.setFont(descFont);
        description.setPosition(WeatherProgram.WINDOW_WIDTH/2 - description.getWidth()/2, icon.getHeight() + tempLabel.getHeight());

        add(description);
    }
}
