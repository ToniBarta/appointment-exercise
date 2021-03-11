package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

import static appointment.PrintingUtil.printMainMenu;
import static appointment.PrintingUtil.printSlots;

public class AppointmentMain {

    public static List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

    public static void main(String[] args) {
        generateStaticAppointments(appointmentSlotList);
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
                while (!InputValidation.validateDateInput(fromDate)) {
                    fromDate = scanner.nextLine();
                }

                System.out.println("Input the end date: ");
                String toDate = scanner.nextLine();
                while (!InputValidation.validateDateInput(toDate)) {
                    toDate = scanner.nextLine();
                }

                List<AppointmentSlot> availableSlots = RangeSearchImpl.searchInRange(fromDate, toDate, appointmentSlotList);
                printSlots(availableSlots);
            }

            if (selectedOption.equals("3")) {
                System.out.printf("Today's date is: %s - %s %n", LocalDate.now(), LocalDate.now().getDayOfWeek());

                System.out.println("On what date do you want the booking to be? ");
                String date = scanner.nextLine();
                while (!InputValidation.validateDateInput(date)) {
                    date = scanner.nextLine();
                }

                System.out.println("From what hour? eg. hh:mm");
                String fromHour = scanner.nextLine();
                while (!InputValidation.validateHourInput(date)) {
                    fromHour = scanner.nextLine();
                }

                System.out.println("Until what hour? eg hh:mm");
                String toHour = scanner.nextLine();
                while (!InputValidation.validateHourInput(date)) {
                    toHour = scanner.nextLine();
                }

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

    // I wanted to make this a random generator from today's day but it would have taken some more time.
    private static void generateStaticAppointments(List<AppointmentSlot> appointments) {
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
}

