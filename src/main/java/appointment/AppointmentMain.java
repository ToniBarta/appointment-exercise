package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

public class AppointmentMain {

    public static List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

    public static void main(String[] args) {

//        LocalDateTime app1From = LocalDateTime.of(2021, 2, 4, 10,0);
//        LocalDateTime app1To = LocalDateTime.of(2021, 2, 4, 10,30);
//        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));
//
//
//        LocalDateTime app2From = LocalDateTime.of(2021, 2, 5, 11,0);
//        LocalDateTime app2To = LocalDateTime.of(2021, 2, 5, 11,30);
//        appointmentSlotList.add(new AppointmentSlot(app2From, app2To));
//
//
//        LocalDateTime app3From = LocalDateTime.of(2021, 2, 5, 15,0);
//        LocalDateTime app3To = LocalDateTime.of(2021, 2, 5, 15,30);
//        appointmentSlotList.add(new AppointmentSlot(app3From, app3To));
//
//
//        LocalDateTime app4From = LocalDateTime.of(2021, 2, 6, 10,0);
//        LocalDateTime app4To = LocalDateTime.of(2021, 2, 6, 10,30);
//        appointmentSlotList.add(new AppointmentSlot(app4From, app4To));
//
//
//        LocalDateTime app5From = LocalDateTime.of(2021, 2, 6, 11,0);
//        LocalDateTime app5To = LocalDateTime.of(2021, 2, 6, 11,30);
//        appointmentSlotList.add(new AppointmentSlot(app5From, app5To));
//
//
//        LocalDateTime app6From = LocalDateTime.of(2021, 2, 6, 17,30);
//        LocalDateTime app6To = LocalDateTime.of(2021, 2, 6, 18,0);
//        appointmentSlotList.add(new AppointmentSlot(app6From, app6To));

        generateRandomAppointments(appointmentSlotList);

        System.out.println("-------------------------------------------");
        System.out.println("Welcome to Toni's minimal appointment system");
        System.out.println("-------------------------------------------");
        System.out.println("Here are a couple of thing that you can do.");
        System.out.println("1 See current bookings");
        System.out.println("2 See available slots");
        System.out.println("3 Make a booking");
        System.out.println("Please enter a number as described above to continue: eg. 1: ");

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

                List<AppointmentSlot> availableSlots = RangeSearchImpl.searchInRange(fromDate, toDate, appointmentSlotList);
                printSlots(availableSlots);
            }

            if (selectedOption.equals("3")) {

            }
        }
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

