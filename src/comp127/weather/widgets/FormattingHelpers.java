package comp127.weather.widgets;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilities to help widgets convert numbers and dates to strings.
 */
@SuppressWarnings("WeakerAccess")
public class FormattingHelpers {
    /**
     * Converts a number to a string with one digit past the decimal point, e.g. "312.3".
     */
    public static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("#0.0");

    /**
     * Converts a date to a string showing the date and day of week in abbreviated form,
     * e.g. "Mon, Oct 14".
     */
    public static final DateFormat WEEKDAY_AND_NAME = new SimpleDateFormat("E, MMM d");

    /**
     * Converts a date to a string showing the 12-hour time of day, e.g. "1:46 PM".
     */
    public static final DateFormat TIME_OF_DAY = new SimpleDateFormat("h:mm a");

    /**
     * Converts the given number to a string with one digit past the decimal point, or returns "–"
     * if given null.
     */
    public static String formatDecimal(Double x) {
        if (x == null) {
            return "–";
        }
        return ONE_DECIMAL_PLACE.format(x);
    }

    /**
     * Converts the given number to a string showing the date and day of week, or returns "–" if
     * given null.
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "–";
        }
        return WEEKDAY_AND_NAME.format(date);
    }

    /**
     * Converts the given number to a string showing the time of day, or returns "–" if given null.
     */
    public static String formatTimeOfDay(Date date) {
        if (date == null) {
            return "–";
        }
        return TIME_OF_DAY.format(date);
    }
}
