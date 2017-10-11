import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;

public class Test {

    public static void main(String[] args)
            throws IOException, MalformedURLException, JSONException {

        // declaring object of "OpenWeatherMap" class
        OpenWeatherMap owm = new OpenWeatherMap("d6a22c9835563a57b372e6515fd8ec2b");

        // getting current weather data for the "London" city
        CurrentWeather cwd = owm.currentWeatherByCityName("Saint Paul, MN");

        //printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());

        // printing the max./min. temperature
        System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
                + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");

        System.out.println(cwd.getWeatherInstance(0).getWeatherDescription());
    }
}