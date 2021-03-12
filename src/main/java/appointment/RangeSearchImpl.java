package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static appointment.AppointmentConst.*;

public class RangeSearchImpl {

    public static List<AppointmentSlot> searchInRange(String from, String to, List<AppointmentSlot> appointmentSlotList) {
        LocalDate fromSearchRangeLocalDate = createLocalDate(from);
        LocalDate toSearchRangeLocalDate = createLocalDate(to);

        Comparator<AppointmentSlot> compareByLocaleDateTime = Comparator.comparing(AppointmentSlot::getFrom);
        appointmentSlotList.sort(compareByLocaleDateTime);

        List<AppointmentSlot> appointmentsBetweenRangeList = getAppointmentsBetweenRange(appointmentSlotList, fromSearchRangeLocalDate, toSearchRangeLocalDate);
        HashMap<LocalDate, List<AppointmentSlot>> sameDayAppointmentsMap = getSameDayAppointmentsMap(appointmentsBetweenRangeList);

        List<AppointmentSlot> freeAppointments = new ArrayList<>();

        for (LocalDate localDateKey : sameDayAppointmentsMap.keySet()) {
            List<AppointmentSlot> appointmentsOfTheDay = sameDayAppointmentsMap.get(localDateKey);

            if (appointmentsOfTheDay.size() == 1) {
                createFreeSlotsWhenOnlyOneAppointmentPerDay(freeAppointments, appointmentsOfTheDay);
            }

            if (appointmentsOfTheDay.size() > 1) {
                createFreeSlotsForTheDay(freeAppointments, appointmentsOfTheDay);
            }
        }
        return freeAppointments;
    }

    protected static void createFreeSlotsForTheDay(List<AppointmentSlot> freeAppointments, List<AppointmentSlot> appointmentsOfTheDay) {
        AppointmentSlot appointmentSlot = appointmentsOfTheDay.get(0);
        LocalDate appointmentSlotLocalDateFrom = appointmentSlot.getFrom().toLocalDate();

        List<AppointmentSlot> appointmentsOfTheDayWithLunch = new ArrayList<>(List.copyOf(appointmentsOfTheDay));
        // adding lunch to the appointment list
        appointmentsOfTheDayWithLunch.add(new AppointmentSlot(
                LocalDateTime.of(appointmentSlotLocalDateFrom, START_OF_LUNCH),
                LocalDateTime.of(appointmentSlotLocalDateFrom, END_OF_LUNCH)));

        // extract in a method
        Comparator<AppointmentSlot> compareByLocaleDateTimeAgain = Comparator.comparing(AppointmentSlot::getFrom);
        appointmentsOfTheDayWithLunch.sort(compareByLocaleDateTimeAgain);

        boolean freeSlotFromStartOfDay = true;
        for (int index = 0; index < appointmentsOfTheDayWithLunch.size() - 1; index++) {
            AppointmentSlot currentApp = appointmentsOfTheDayWithLunch.get(index);
            LocalTime currentLocalTimeFrom = currentApp.getFrom().toLocalTime();
            LocalDate currentLocaleDateFrom = currentApp.getFrom().toLocalDate();

            AppointmentSlot nextApp = appointmentsOfTheDayWithLunch.get(index + 1);

            if (currentLocalTimeFrom.isAfter(START_OF_DAY) && freeSlotFromStartOfDay) {
                freeAppointments.add(new AppointmentSlot(LocalDateTime.of(currentLocaleDateFrom, START_OF_DAY), currentApp.getFrom()));
                if (!currentApp.getTo().equals(nextApp.getFrom())) {
                    freeAppointments.add(new AppointmentSlot(currentApp.getTo(), nextApp.getFrom()));
                }
                freeSlotFromStartOfDay = false;
            } else {
                if (currentLocalTimeFrom.equals(START_OF_DAY)) {
                    freeSlotFromStartOfDay = false;
                }
                if (!currentApp.getTo().equals(nextApp.getFrom())) {
                    freeAppointments.add(new AppointmentSlot(currentApp.getTo(), nextApp.getFrom()));
                }
            }
        }
        AppointmentSlot lastApp = appointmentsOfTheDayWithLunch.get(appointmentsOfTheDayWithLunch.size() - 1);
        if (lastApp.getTo().isBefore(LocalDateTime.of(lastApp.getFrom().toLocalDate(), END_OF_DAY))) {
            freeAppointments.add(new AppointmentSlot(lastApp.getTo(), LocalDateTime.of(lastApp.getFrom().toLocalDate(), END_OF_DAY)));
        }
    }

    protected static List<AppointmentSlot> createFreeSlotsWhenOnlyOneAppointmentPerDay(List<AppointmentSlot> freeAppointments, List<AppointmentSlot> appointmentsOfTheDay) {
        AppointmentSlot appointmentSlot = appointmentsOfTheDay.get(0);

        LocalTime localTimeFrom = appointmentSlot.getFrom().toLocalTime();
        LocalTime localTimeTo = appointmentSlot.getTo().toLocalTime();

        if (localTimeFrom.equals(START_OF_DAY) && localTimeTo.equals(START_OF_LUNCH)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_LUNCH), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_DAY)));
            return freeAppointments;
        }

        if (localTimeFrom.equals(END_OF_LUNCH) && localTimeTo.equals(END_OF_DAY)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_DAY), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_LUNCH)));
            return freeAppointments;
        }

        if (localTimeFrom.equals(START_OF_DAY)) {
            freeAppointments.add(new AppointmentSlot(appointmentSlot.getTo(), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_LUNCH)));
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_LUNCH), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_DAY)));
            return freeAppointments;
        }

        if (localTimeTo.equals(START_OF_LUNCH)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_DAY), (appointmentSlot.getFrom())));
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_LUNCH), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_DAY)));
            return freeAppointments;
        }

        if (localTimeFrom.equals(END_OF_LUNCH)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_DAY), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_LUNCH)));
            freeAppointments.add(new AppointmentSlot(appointmentSlot.getTo(), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_DAY)));
            return freeAppointments;
        }

        if (localTimeTo.equals(END_OF_DAY)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_DAY), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_LUNCH)));
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_LUNCH), appointmentSlot.getFrom()));
            return freeAppointments;
        }

        if (localTimeFrom.isBefore(START_OF_LUNCH)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_DAY), appointmentSlot.getFrom()));
            freeAppointments.add(new AppointmentSlot(appointmentSlot.getTo(), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_LUNCH)));
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_LUNCH), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_DAY)));
            return freeAppointments;
        }

        if (localTimeFrom.isAfter(END_OF_LUNCH)) {
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_DAY), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), START_OF_LUNCH)));
            freeAppointments.add(new AppointmentSlot(LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_LUNCH), appointmentSlot.getFrom()));
            freeAppointments.add(new AppointmentSlot(appointmentSlot.getTo(), LocalDateTime.of(appointmentSlot.getFrom().toLocalDate(), END_OF_DAY)));
            return freeAppointments;
        }

        return freeAppointments;
    }

    protected static HashMap<LocalDate, List<AppointmentSlot>> getSameDayAppointmentsMap(List<AppointmentSlot> appointmentsBetweenRangeList) {
        HashMap<LocalDate, List<AppointmentSlot>> mapOfSameDayAppointments = new HashMap<>();
        for (AppointmentSlot appointmentSlot : appointmentsBetweenRangeList) {
            LocalDate localDateOfAppointmentKey = appointmentSlot.getFrom().toLocalDate();

            if (mapOfSameDayAppointments.containsKey(localDateOfAppointmentKey)) {
                List<AppointmentSlot> sameDayAppointmentSlots = mapOfSameDayAppointments.get(localDateOfAppointmentKey);
                sameDayAppointmentSlots.add(appointmentSlot);
                mapOfSameDayAppointments.put(localDateOfAppointmentKey, sameDayAppointmentSlots);
            } else {
                ArrayList<AppointmentSlot> appointmentSlots = new ArrayList<>();
                appointmentSlots.add(appointmentSlot);
                mapOfSameDayAppointments.put(localDateOfAppointmentKey, appointmentSlots);
            }
        }
        return mapOfSameDayAppointments;
    }

    private static List<AppointmentSlot> getAppointmentsBetweenRange
            (List<AppointmentSlot> appointmentSlotList, LocalDate fromSearchRangeLocalDate, LocalDate toSearchRangeLocalDate) {
        return appointmentSlotList.stream()
                .filter(appointmentSlot -> isBetweenSearchRange(fromSearchRangeLocalDate, toSearchRangeLocalDate, appointmentSlot.getFrom().toLocalDate()))
                .collect(Collectors.toList());
    }

    protected static boolean isBetweenSearchRange(LocalDate fromSearchRangeLocalDate, LocalDate
            toSearchRangeLocalDate, LocalDate appointmentFromLocaleDate) {
        return fromSearchRangeLocalDate.isBefore(appointmentFromLocaleDate) && toSearchRangeLocalDate.isAfter(appointmentFromLocaleDate)
                || (fromSearchRangeLocalDate.isBefore(appointmentFromLocaleDate) && toSearchRangeLocalDate.isEqual(appointmentFromLocaleDate))
                || (fromSearchRangeLocalDate.isEqual(appointmentFromLocaleDate) && toSearchRangeLocalDate.isAfter(appointmentFromLocaleDate))
                || (fromSearchRangeLocalDate.isEqual(appointmentFromLocaleDate) && toSearchRangeLocalDate.isEqual(appointmentFromLocaleDate));
    }

    protected static LocalDate createLocalDate(String stringDate) {
        try {
            String[] regexSplitString = stringDate.split("-");
            return LocalDate.of(Integer.parseInt(regexSplitString[0]), Integer.parseInt(regexSplitString[1]), Integer.parseInt(regexSplitString[2]));
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("The date should have the following format: 2021-01-05");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    protected static LocalTime crateLocalTime(String stringTime) {
        try {
            String[] regexSplitString = stringTime.split(":");
            return LocalTime.of(Integer.parseInt(regexSplitString[0]), Integer.parseInt(regexSplitString[1]));
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("The time should have the following format: 9:00 or 14:30");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
