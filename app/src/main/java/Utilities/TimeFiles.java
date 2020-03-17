package Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public interface TimeFiles {

    static Date formatStringToDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        return df.parse(date);
    }

}
