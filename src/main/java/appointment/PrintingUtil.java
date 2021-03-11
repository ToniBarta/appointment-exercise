package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class PrintingUtil {

    public static void printMainMenu() {
        System.out.println("-------------------------------------------");
        System.out.println("Welcome to Toni's minimal appointment system");
        System.out.println("-------------------------------------------");
        System.out.println("Here are a couple of thing that you can do.");
        System.out.println("1 See current bookings");
        System.out.println("2 See available slots");
        System.out.println("3 Make a booking");
        System.out.println("4 See this main menu again?");
        System.out.println("Type exit in order to exit");
        System.out.println("-------------------------------------------");

        System.out.println("Please enter a number as described above to continue: eg. 1 and press ENTER");
    }

    public static void printSlots(List<AppointmentSlot> appointmentSlotList) {
        HashMap<LocalDate, List<AppointmentSlot>> sameDayAppointmentsMap = RangeSearchImpl.getSameDayAppointmentsMap(appointmentSlotList);

        // sort the key by dates from lower to higher
        TreeSet<LocalDate> localDates = new TreeSet<>(sameDayAppointmentsMap.keySet());

        localDates.forEach(key -> {
            List<AppointmentSlot> appointmentSlots = sameDayAppointmentsMap.get(key);
            System.out.printf("Date: %s :  | " ,key);
            for (AppointmentSlot slot: appointmentSlots) {
                System.out.printf("%s - %s  | ", slot.getFrom().toLocalTime(), slot.getTo().toLocalTime());
            }
            System.out.println();
        });
    }
}
