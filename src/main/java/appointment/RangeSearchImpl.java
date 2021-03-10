package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static appointment.AppoinmentConsts.*;

public class RangeSearchImpl {

    public static List<AppointmentSlot> searchInRange2(String from, String to, List<AppointmentSlot> appointmentSlotList) {
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

        }
        System.out.println(freeAppointments);
        return freeAppointments;
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

        return freeAppointments;
    }

    private static HashMap<LocalDate, List<AppointmentSlot>> getSameDayAppointmentsMap(List<AppointmentSlot> appointmentsBetweenRangeList) {
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

    protected static boolean isLunchTime(LocalTime ongoingLocalTime) {
        return ongoingLocalTime.equals(START_OF_LUNCH) || (ongoingLocalTime.isAfter(START_OF_LUNCH) && ongoingLocalTime.isBefore(END_OF_LUNCH));
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
}
