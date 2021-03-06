package appointment;

import java.time.LocalTime;

public class AppoinmentConsts {

    public static LocalTime START_OF_DAY = LocalTime.of(8, 0);
    public static LocalTime END_OF_DAY = LocalTime.of(18, 0);
    public static LocalTime START_OF_LUNCH = LocalTime.of(12, 0);
    public static LocalTime END_OF_LUNCH = LocalTime.of(13, 0);
    public static int TIME_SLOT_OF_30_MINS = 30;
}
