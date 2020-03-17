package converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    public static String stringToLong() {
        long tsLong = System.currentTimeMillis() / 1000;
        return Long.toString(tsLong);
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();

    }

}

