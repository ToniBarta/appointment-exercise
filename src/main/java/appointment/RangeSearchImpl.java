package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static appointment.AppoinmentConsts.*;

public class RangeSearchImpl {

    public static void searchInRange(String from, String to, List<AppointmentSlot> appointmentSlotList) {
        LocalDate fromSearchRangeLocalDate = createLocalDate(from.split("-"));
        LocalDate toSearchRangeLocalDate = createLocalDate(to.split("-"));

        List<AppointmentSlot> freeSlots = new ArrayList<>();

        for (AppointmentSlot appointmentSlot : appointmentSlotList) {

            LocalDate appointmentFromLocaleDate = appointmentSlot.getFrom().toLocalDate();
            if (isBetweenSearchRange(fromSearchRangeLocalDate, toSearchRangeLocalDate, appointmentFromLocaleDate)) {

                LocalTime ongoingLocalTime = START_OF_DAY;

                while (ongoingLocalTime.isBefore(END_OF_DAY)) {
                    LocalDateTime endOfAppointment = appointmentSlot.getTo();

                    if (!isLunchTime(ongoingLocalTime)) {

                        // current time plus 30 is not equal to the end of an appointment
                        LocalDateTime futureAppointmentTime = ongoingLocalTime.plusMinutes(TIME_SLOT_OF_30_MINS).atDate(appointmentFromLocaleDate);

                        if (!futureAppointmentTime.equals(endOfAppointment)) {
                            //we have a free slot from currentDayTime + 30 mins
                            freeSlots.add(new AppointmentSlot(ongoingLocalTime.atDate(appointmentFromLocaleDate), futureAppointmentTime));
                        }
                    }
                    ongoingLocalTime = ongoingLocalTime.plusMinutes(TIME_SLOT_OF_30_MINS);
                }
            }
        }

        System.out.println(freeSlots);
    }

    protected static boolean isLunchTime(LocalTime ongoingLocalTime) {
        return ongoingLocalTime.equals(START_OF_LUNCH) || (ongoingLocalTime.isAfter(START_OF_LUNCH) && ongoingLocalTime.isBefore(END_OF_LUNCH));
    }

    protected static boolean isBetweenSearchRange(LocalDate fromSearchRangeLocalDate, LocalDate toSearchRangeLocalDate, LocalDate appointmentFromLocaleDate) {
        return fromSearchRangeLocalDate.isBefore(appointmentFromLocaleDate) && toSearchRangeLocalDate.isAfter(appointmentFromLocaleDate)
                || (fromSearchRangeLocalDate.isBefore(appointmentFromLocaleDate) && toSearchRangeLocalDate.isEqual(appointmentFromLocaleDate))
                || (fromSearchRangeLocalDate.isEqual(appointmentFromLocaleDate) && toSearchRangeLocalDate.isAfter(appointmentFromLocaleDate))
                || (fromSearchRangeLocalDate.isEqual(appointmentFromLocaleDate) && toSearchRangeLocalDate.isEqual(appointmentFromLocaleDate));
    }

    protected static LocalDate createLocalDate(String[] regexSplitString) {
        return LocalDate.of(Integer.parseInt(regexSplitString[0]), Integer.parseInt(regexSplitString[1]), Integer.parseInt(regexSplitString[2]));
    }
}
