package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.List;

public class AppointmentMain {

    public static List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

    public static void main(String[] args) {
        generateRandomAppointments(appointmentSlotList);

        printMainMenu();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNext("exit")) {
            String selectedOption = scanner.nextLine();

            if (selectedOption.equals("1")) {
                printSlots(appointmentSlotList);
            }

            if (selectedOption.equals("2")) {
                System.out.printf("Can you select a range between %s and %s ?%n", LocalDate.now().toString(), LocalDate.now().plusDays(5).toString());
                System.out.println("Input the start date: ");
                String fromDate = scanner.nextLine();
                System.out.println("Input the end date: ");
                String toDate = scanner.nextLine();

//                add Validation

                List<AppointmentSlot> availableSlots = RangeSearchImpl.searchInRange(fromDate, toDate, appointmentSlotList);
                printSlots(availableSlots);
            }

            if (selectedOption.equals("3")) {
                System.out.printf("Today's date is: %s - %s %n", LocalDate.now(), LocalDate.now().getDayOfWeek());
                System.out.println("On what date do you want the booking to be? ");
                String date = scanner.nextLine();
                System.out.println("From what hour?");
                String fromHour = scanner.nextLine();
                System.out.println("Until what hour?");
                String toHour = scanner.nextLine();

//                add Validation
                LocalDate localDate = RangeSearchImpl.createLocalDate(date);
                LocalTime fromTime = RangeSearchImpl.crateLocalTime(fromHour);
                LocalTime toTime = RangeSearchImpl.crateLocalTime(toHour);

                AppointmentSlot newAppointment = new AppointmentSlot(LocalDateTime.of(localDate, fromTime), LocalDateTime.of(localDate, toTime));

                // this will not fully work
                if (appointmentSlotList.contains(newAppointment)) {
                    System.out.println("This time for this day is already taken. Please press 2 to view available slots.");
                } else {
                    System.out.printf("Your booking is confirmed on the following date: %s between: %s and %s %n", localDate, fromTime, toTime);
                }
            }

            if (selectedOption.equals("4")) {
                printMainMenu();
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("-------------------------------------------");
        System.out.println("Welcome to Toni's minimal appointment system");
        System.out.println("-------------------------------------------");
        System.out.println("Here are a couple of thing that you can do.");
        System.out.println("1 See current bookings");
        System.out.println("2 See available slots");
        System.out.println("3 Make a booking");
        System.out.println("4 See this main menu again?");
        System.out.println("-------------------------------------------");

        System.out.println("Please enter a number as described above to continue: eg. 1 and press ENTER");
    }

    private static void printSlots(List<AppointmentSlot> appointmentSlotList) {
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

    private static void generateRandomAppointments(List<AppointmentSlot> appointments) {
        LocalDate now = LocalDate.now();

        appointments.add(new AppointmentSlot(now.atTime(8, 0), now.atTime(9, 30)));
        appointments.add(new AppointmentSlot(now.atTime(11, 0), now.atTime(11, 30)));
        appointments.add(new AppointmentSlot(now.atTime(17, 0), now.atTime(18, 0)));

        now = now.plusDays(1);
        appointments.add(new AppointmentSlot(now.atTime(11, 30), now.atTime(12, 0)));
        appointments.add(new AppointmentSlot(now.atTime(13, 0), now.atTime(14, 30)));

        now = now.plusDays(1);
        appointments.add(new AppointmentSlot(now.atTime(8, 30), now.atTime(12, 0)));

        now = now.plusDays(1);
        appointments.add(new AppointmentSlot(now.atTime(8, 30), now.atTime(9, 0)));
        appointments.add(new AppointmentSlot(now.atTime(13, 30), now.atTime(14, 30)));
    }

    private void createAppointment(List<AppointmentSlot> appointmentSlotList,
                                   int dayFrom, int hourFrom, int minuteFrom,
                                   int dayTo, int hourTo, int minuteTo) {
        LocalDateTime from = LocalDateTime.of(2021, Month.MAY, dayFrom, hourFrom, minuteFrom);
        LocalDateTime to = LocalDateTime.of(2021, Month.MAY, dayTo, hourTo, minuteTo);
        appointmentSlotList.add(new AppointmentSlot(from, to));
    }

}

