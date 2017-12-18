package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:11
 */
public final class Time {

    private long nanoseconds;

    public Time(long nanoseconds) {
        this.nanoseconds = nanoseconds;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date(nanoseconds / 1000000));
    }

}
