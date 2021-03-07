package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static appointment.AppoinmentConsts.*;

public class RangeSearchImpl {

    public static List<AppointmentSlot> searchInRange2(String from, String to, List<AppointmentSlot> appointmentSlotList) {
        LocalDate fromSearchRangeLocalDate = createLocalDate(from);
        LocalDate toSearchRangeLocalDate = createLocalDate(to);

        Comparator<AppointmentSlot> compareByLocaleDateTime = Comparator.comparing(AppointmentSlot::getFrom);
        appointmentSlotList.sort(compareByLocaleDateTime);

        List<AppointmentSlot> freeSlots = new ArrayList<>();
        HashMap<LocalDate, Boolean> visitedDay = new HashMap();

        for (int index=0; index < appointmentSlotList.size(); index++) {
            AppointmentSlot appointmentSlot = appointmentSlotList.get(index);
            LocalDate appointmentFromLocaleDate = appointmentSlot.getFrom().toLocalDate();

            if (isBetweenSearchRange(fromSearchRangeLocalDate, toSearchRangeLocalDate, appointmentFromLocaleDate)) {

                if (!visitedDay.isEmpty() && visitedDay.keySet().contains(appointmentSlot.getFrom().toLocalDate()) ) {
                    //skip this day
                } else {
                    int nextApp = index + 1;

                    if (START_OF_DAY.isBefore(appointmentSlot.getFrom().toLocalTime())) {
                        LocalDateTime fromFreeTime = LocalDateTime.of(appointmentFromLocaleDate, START_OF_DAY);
                        LocalDateTime toFreeTime = appointmentSlot.getFrom();
                        freeSlots.add(new AppointmentSlot(fromFreeTime, toFreeTime));

                        LocalDateTime rememberEndOfAppointment = appointmentSlot.getTo();

                        // while next slot is same day cont with finding empty app slots
                        while (nextApp < appointmentSlotList.size() && appointmentSlotList.get(nextApp).getFrom().toLocalDate().isEqual(rememberEndOfAppointment.toLocalDate())) {
                            freeSlots.add(new AppointmentSlot(rememberEndOfAppointment, appointmentSlotList.get(nextApp).getFrom()));
                            rememberEndOfAppointment = appointmentSlotList.get(nextApp).getTo();
                            nextApp = nextApp + 1;
                        }

                        // when this while loop is over it means that we have went trough all the day
                        // and we need to check if there is some time until the end of the day
                        if (rememberEndOfAppointment.toLocalTime().isBefore(END_OF_DAY)) {
                            // we have another free slot
                            freeSlots.add(new AppointmentSlot(rememberEndOfAppointment, LocalDateTime.of(appointmentFromLocaleDate, END_OF_DAY)));
                        }

                        visitedDay.put(appointmentFromLocaleDate, true);
                    } else {
                        LocalDateTime rememberEndOfAppointment = appointmentSlot.getTo();

                        // while next slot is same day cont with finding empty app slots
                        while (nextApp < appointmentSlotList.size() && appointmentSlotList.get(nextApp).getFrom().toLocalDate().isEqual(rememberEndOfAppointment.toLocalDate())) {
                            freeSlots.add(new AppointmentSlot(rememberEndOfAppointment, appointmentSlotList.get(nextApp).getFrom()));
                            rememberEndOfAppointment = appointmentSlotList.get(nextApp).getTo();
                            nextApp = nextApp + 1;
                        }

                        // when this while loop is over it means that we have went trough all the day
                        // and we need to check if there is some time until the end of the day
                        if (rememberEndOfAppointment.toLocalTime().isBefore(END_OF_DAY)) {
                            // we have another free slot
                            freeSlots.add(new AppointmentSlot(rememberEndOfAppointment, LocalDateTime.of(appointmentFromLocaleDate, END_OF_DAY)));
                        }
                        visitedDay.put(appointmentFromLocaleDate, true);
                    }
                }

            }

        }

        System.out.println(freeSlots);
        return freeSlots;
    }



    public static List<AppointmentSlot> searchInRange(String from, String to, List<AppointmentSlot> appointmentSlotList) {
        LocalDate fromSearchRangeLocalDate = createLocalDate(from);
        LocalDate toSearchRangeLocalDate = createLocalDate(to);

        List<AppointmentSlot> freeSlots = new ArrayList<>();

        for (AppointmentSlot appointmentSlot : appointmentSlotList) {

            LocalDate appointmentFromLocaleDate = appointmentSlot.getFrom().toLocalDate();
            if (isBetweenSearchRange(fromSearchRangeLocalDate, toSearchRangeLocalDate, appointmentFromLocaleDate)) {

                LocalTime ongoingLocalTime = START_OF_DAY;
                while (ongoingLocalTime.isBefore(END_OF_DAY)) {
                    LocalDateTime startOfAppointment = appointmentSlot.getFrom();
                    LocalDateTime endOfAppointment = appointmentSlot.getTo();

                    if (!isLunchTime(ongoingLocalTime)) {
                        // current time plus 30
                        LocalDateTime futureAppointmentTime = ongoingLocalTime.plusMinutes(TIME_SLOT_OF_30_MINS).atDate(appointmentFromLocaleDate);

                        if (futureAppointmentTime.isAfter(startOfAppointment) && futureAppointmentTime.isBefore(endOfAppointment)) {
//                        if (!futureAppointmentTime.equals(endOfAppointment)) {
                            //we have a free slot from ongoingLocalTime + 30 min
                            freeSlots.add(new AppointmentSlot(ongoingLocalTime.atDate(appointmentFromLocaleDate), futureAppointmentTime));
                        }
                    }
                    ongoingLocalTime = ongoingLocalTime.plusMinutes(TIME_SLOT_OF_30_MINS);
                }
            } else {
                // if cannot find any booked values for that day then the day is free

            }
        }

        System.out.println(freeSlots);
        return freeSlots;
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

    protected static LocalDate createLocalDate(String stringDate) {
        try {
            String[] regexSplitString = stringDate.split("-");
            return LocalDate.of(Integer.parseInt(regexSplitString[0]), Integer.parseInt(regexSplitString[1]), Integer.parseInt(regexSplitString[2]));
        }
        catch(NumberFormatException exception) {
            throw new NumberFormatException("The date should have the following format: 2021-01-05");
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
