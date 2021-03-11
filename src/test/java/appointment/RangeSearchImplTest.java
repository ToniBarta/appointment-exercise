package appointment;

import domain.AppointmentSlot;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static appointment.AppointmentConst.*;

public class RangeSearchImplTest {

    public static final int YEAR = 2021;

    private List<AppointmentSlot> freeSlots = new ArrayList<>();

    @Test
    public void oneAppointmentAtStartOfDayUntilStartOfLunch() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 8, 0,
                5, 12, 0);

        LocalDate localDate = appointmentSlotList.get(0).getFrom().toLocalDate();

        List<AppointmentSlot> freeSlotsWhenOnlyOneAppointmentPerDay = RangeSearchImpl.createFreeSlotsWhenOnlyOneAppointmentPerDay(freeSlots, appointmentSlotList);
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.size(), 1);

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(YEAR, Month.MAY, 5, 13, 0));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(localDate, END_OF_DAY));
    }

    @Test
    public void oneAppointmentAtStartOfDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 8, 0,
                5, 10, 0);

        LocalDate localDate = appointmentSlotList.get(0).getFrom().toLocalDate();

        List<AppointmentSlot> freeSlotsWhenOnlyOneAppointmentPerDay = RangeSearchImpl.createFreeSlotsWhenOnlyOneAppointmentPerDay(freeSlots, appointmentSlotList);
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.size(), 2);

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(YEAR, Month.MAY, 5, 10, 0));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(localDate, START_OF_LUNCH));

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getFrom(), LocalDateTime.of(localDate, END_OF_LUNCH));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getTo(), LocalDateTime.of(localDate, END_OF_DAY));
    }

    @Test
    public void oneAppointmentThatEndsAtStartOfLunch() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 10, 0,
                5, 12, 0);

        LocalDate localDate = appointmentSlotList.get(0).getFrom().toLocalDate();

        List<AppointmentSlot> freeSlotsWhenOnlyOneAppointmentPerDay = RangeSearchImpl.createFreeSlotsWhenOnlyOneAppointmentPerDay(freeSlots, appointmentSlotList);
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.size(), 2);

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(localDate, START_OF_DAY));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(YEAR, Month.MAY, 5, 10, 0));

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getFrom(), LocalDateTime.of(localDate, END_OF_LUNCH));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getTo(), LocalDateTime.of(localDate, END_OF_DAY));
    }

    @Test
    public void oneAppointmentThatStartsAtEndOfLunch() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 13, 0,
                5, 15, 0);

        LocalDate localDate = appointmentSlotList.get(0).getFrom().toLocalDate();

        List<AppointmentSlot> freeSlotsWhenOnlyOneAppointmentPerDay = RangeSearchImpl.createFreeSlotsWhenOnlyOneAppointmentPerDay(freeSlots, appointmentSlotList);
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.size(), 2);

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(localDate, START_OF_DAY));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(localDate, START_OF_LUNCH));

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getFrom(), LocalDateTime.of(YEAR, Month.MAY, 5, 15, 0));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getTo(), LocalDateTime.of(localDate, END_OF_DAY));
    }

    @Test
    public void oneAppointmentThatStartsAtEndOfLunchAndEndsAtEndOfDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 13, 0,
                5, 18, 0);

        LocalDate localDate = appointmentSlotList.get(0).getFrom().toLocalDate();

        List<AppointmentSlot> freeSlotsWhenOnlyOneAppointmentPerDay = RangeSearchImpl.createFreeSlotsWhenOnlyOneAppointmentPerDay(freeSlots, appointmentSlotList);
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.size(), 1);

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(localDate, START_OF_DAY));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(localDate, START_OF_LUNCH));
    }

    @Test
    public void oneAppointmentThatStartsAtEndOfDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 15, 0,
                5, 18, 0);

        LocalDate localDate = appointmentSlotList.get(0).getFrom().toLocalDate();

        List<AppointmentSlot> freeSlotsWhenOnlyOneAppointmentPerDay = RangeSearchImpl.createFreeSlotsWhenOnlyOneAppointmentPerDay(freeSlots, appointmentSlotList);
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.size(), 2);

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(localDate, START_OF_DAY));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(localDate, START_OF_LUNCH));

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getFrom(), LocalDateTime.of(localDate, END_OF_LUNCH));
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getTo(), LocalDateTime.of(YEAR, Month.MAY, 5, 15, 0));
    }

    @Test
    public void whenAppointmentIsBeforeSearchRange() {
        LocalDate appointmentBeforeSearchRange = LocalDate.of(YEAR, 5, 1);
        LocalDate fromSearchRange = LocalDate.of(YEAR, 5, 2);
        LocalDate toSearchRange = LocalDate.of(YEAR, 5, 10);

        Assert.assertFalse(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentBeforeSearchRange));
    }

    @Test
    public void whenAppointmentEqualsWithFromSearchRange() {
        LocalDate appointmentSameAsFromRange = LocalDate.of(YEAR, 5, 1);
        LocalDate fromSearchRange = LocalDate.of(YEAR, 5, 1);
        LocalDate toSearchRange = LocalDate.of(YEAR, 5, 10);

        Assert.assertTrue(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentSameAsFromRange));
    }

    @Test
    public void whenAppointmentIsBetweenSearchRange() {
        LocalDate appointmentInBetweenSearchRange = LocalDate.of(YEAR, 5, 5);
        LocalDate fromSearchRange = LocalDate.of(YEAR, 5, 1);
        LocalDate toSearchRange = LocalDate.of(YEAR, 5, 10);

        Assert.assertTrue(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentInBetweenSearchRange));
    }

    @Test
    public void whenAppointmentIsEqualToToSearchRange() {
        LocalDate appointmentSameAsToRange = LocalDate.of(YEAR, 5, 5);
        LocalDate fromSearchRange = LocalDate.of(YEAR, 5, 1);
        LocalDate toSearchRange = LocalDate.of(YEAR, 5, 5);

        Assert.assertTrue(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentSameAsToRange));
    }

    @Test
    public void whenAppointmentIsAfterSearchRange() {
        LocalDate appointmentAfterSearchRange = LocalDate.of(YEAR, 5, 5);
        LocalDate fromSearchRange = LocalDate.of(YEAR, 5, 1);
        LocalDate toSearchRange = LocalDate.of(YEAR, 5, 4);

        Assert.assertFalse(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentAfterSearchRange));
    }

    @Test(expected = NumberFormatException.class)
    public void createLocalDateWhenDoesNotContainHyphen() {
        String date = "2021/02/05";
        RangeSearchImpl.createLocalDate(date);
    }

    @Test(expected = RuntimeException.class)
    public void createLocalDateWhenDoesNotContainCorrectDates() {
        String date = "2021-15-40";
        RangeSearchImpl.createLocalDate(date);
    }

    @Test
    public void createLocalDate() {
        String date = "2021-01-01";
        LocalDate localDate = RangeSearchImpl.createLocalDate(date);

        LocalDate expectedDate = LocalDate.of(YEAR, 1, 1);
        Assert.assertEquals(localDate, expectedDate);
    }

    @Test
    public void searchInRangeOnTheSameDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 9, 0,
                5, 10, 0);

        createAppointment(appointmentSlotList,
                5, 14, 30,
                5, 15, 0);

        createAppointment(appointmentSlotList,
                5, 16, 30,
                5, 17, 0);

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 5);
    }

    @Test
    public void searchInRangeOnMultipleDays() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 9, 0,
                5, 10, 0);

        createAppointment(appointmentSlotList,
                5, 14, 30,
                5, 15, 0);

        createAppointment(appointmentSlotList,
                5, 16, 30,
                5, 17, 0);

        createAppointment(appointmentSlotList,
                6, 13, 30,
                6, 14, 30);

        createAppointment(appointmentSlotList,
                6, 16, 30,
                6, 18, 0);

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange("2021-05-05", "2021-05-06", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 8);
    }

    private void createAppointment(List<AppointmentSlot> appointmentSlotList,
                                   int dayFrom, int hourFrom, int minuteFrom,
                                   int dayTo, int hourTo, int minuteTo) {
        LocalDateTime from = LocalDateTime.of(YEAR, Month.MAY, dayFrom, hourFrom, minuteFrom);
        LocalDateTime to = LocalDateTime.of(YEAR, Month.MAY, dayTo, hourTo, minuteTo);
        appointmentSlotList.add(new AppointmentSlot(from, to));
    }

    @Test
    public void searchInRangeWhenAppointmentFromStartOfDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(YEAR, 5, 5, 8, 0);
        LocalDateTime app1To = LocalDateTime.of(YEAR, 5, 5, 10, 0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 2);
    }

    @Test
    public void searchInRangeWhenAppointmentIsLastOnTheDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(YEAR, 5, 5, 15, 0);
        LocalDateTime app1To = LocalDateTime.of(YEAR, 5, 5, 18, 0); // use end of day
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 2);
    }

    @Test
    public void getSameDayAppointmentsMapWhenOnlyOneDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 9, 0,
                5, 10, 0);

        createAppointment(appointmentSlotList,
                5, 14, 30,
                5, 15, 0);

        HashMap<LocalDate, List<AppointmentSlot>> sameDayAppointmentsMap = RangeSearchImpl.getSameDayAppointmentsMap(appointmentSlotList);

        LocalDate expectedKey = LocalDate.of(YEAR, Month.MAY, 5);
        Assert.assertEquals(sameDayAppointmentsMap.keySet().size(), 1);
        Assert.assertEquals(sameDayAppointmentsMap.get(expectedKey).size(), 2);
    }

    @Test
    public void getSameDayAppointmentsWhenMultipleDays() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        createAppointment(appointmentSlotList,
                5, 9, 0,
                5, 10, 0);

        createAppointment(appointmentSlotList,
                6, 13, 30,
                6, 14, 30);

        createAppointment(appointmentSlotList,
                6, 16, 30,
                6, 18, 0);

        HashMap<LocalDate, List<AppointmentSlot>> sameDayAppointmentsMap = RangeSearchImpl.getSameDayAppointmentsMap(appointmentSlotList);

        LocalDate expectedKeyDay5 = LocalDate.of(YEAR, Month.MAY, 5);
        LocalDate expectedKeyDay6 = LocalDate.of(YEAR, Month.MAY, 6);

        Assert.assertEquals(sameDayAppointmentsMap.keySet().size(), 2);
        Assert.assertEquals(sameDayAppointmentsMap.get(expectedKeyDay5).size(), 1);
        Assert.assertEquals(sameDayAppointmentsMap.get(expectedKeyDay6).size(), 2);
    }
}
