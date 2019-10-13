package comp127.weather.widgets;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattingHelpers {
    // This is used to format decimal numbers based on the number of digits you want to display
    // e.g. System.out.println(df.format(4.555555)); will print 4.5;
    private static final DecimalFormat oneDecimalPlace = new DecimalFormat("#0.0");
    private static final DateFormat dateFormat = new SimpleDateFormat("E, MMM d");
    private static final DateFormat timeFormat = new SimpleDateFormat("h:mm a");

    static String formatDecimal(Double x) {
        if (x == null) {
            return "–";
        }
        return oneDecimalPlace.format(x);
    }

    static String formatDate(Date date) {
        if (date == null) {
            return "–";
        }
        return dateFormat.format(date);
    }

    static String formatTimeOfDay(Date date) {
        if (date == null) {
            return "–";
        }
        return timeFormat.format(date);
    }
}
