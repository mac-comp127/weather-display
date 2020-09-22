package comp127.weather.widgets;

import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import edu.macalester.graphics.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ForecastWidget implements WeatherWidget {

    private final double size;
    private GraphicsGroup group;

    // TODO: Add instance variables for any UI elements that you create in the constructor but
    //       will need to update later with new data

    private GraphicsGroup boxGroup;  // Holds all the ForecastBox objects

    private List<ForecastBox> boxes = new ArrayList<>();

    public ForecastWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();

        // TODO: Create the various text and image elements you will need (but not the row of boxes
        //       at the bottom; those you will create in update())

        boxGroup = new GraphicsGroup();
        group.add(boxGroup);

        updateLayout();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    public void update(WeatherData data) {
        // TODO: Remove all the existing elements from boxGroup.
        //       HINT: check the javadoc for GraphicsGroup to keep this simple!

        boxes.clear();  // Remove all the old ForecastBoxes from our list

        // TODO: Loop through all the ForecastConditions objects from data, and for each one:
        //       - Wrap it in a new ForecastBox
        //       - Position it to the right of the previous box, wrapping to a new line if you are
        //         past the end of the current one.
        //       - Add the new box to the graphics group
        //       - Add the new box to the `boxes` list

        // TODO: Call selectForecast() with the first ForecastBox, which will update the various
        //       text and icon elements.
    }

    private void selectForecast(ForecastBox box) {
        // TODO: Call setActive() for all the forecast boxes, with true for the selected box and
        //       false for all the others (so that the previously active one becomes inactive).

        // TODO: Get the forecast data from the box, and use it to update the text and icon.

        updateLayout();
    }

    private void updateLayout() {
        // TODO: Place all the elements on the canvas in the correct position
        //       HINT: Use multiples of size instead of absolute pixel measurements to adjust to
        //             different widget sizes.
        //       HINT: Study the methods of GraphicsObject to find different ways of positioning and
        //             measuring graphics objects.
    }

    /**
     * Given a position in the widget, this returns the ForecastBox at that position if one exists
     *
     * @param location pos to check
     * @return null if not over a forecast box
     */
    private ForecastBox getBoxAt(Point location) {
        GraphicsObject obj = group.getElementAt(location);
        if (obj instanceof ForecastBox) {
            return (ForecastBox) obj;
        }
        return null;
    }

    /**
     * Updates the currently displayed forecast information as the mouse moves over the widget.
     * If there is not a ForecastBox at that position, the display does not change.
     */
    @Override
    public void onHover(Point position) {
        // TODO: Check if there is a box at the current mouse position.
        //       If there is, make it the selected forecast.
        //       HINT: Study the methods above! They will help you immensely.
        //       HINT: This should be a small method. If it gets complicated,
        //             you‘ve gone off the rails.
    }
}
