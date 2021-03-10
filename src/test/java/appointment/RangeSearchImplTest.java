package appointment;

import domain.AppointmentSlot;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static appointment.AppoinmentConsts.*;

public class RangeSearchImplTest {

    public static final int YEAR = 2021;
    public static final int MONTH_05 = 5;

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

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(YEAR, MONTH_05, 5, 13, 0));
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

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getFrom(), LocalDateTime.of(YEAR, MONTH_05, 5, 10, 0));
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
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(0).getTo(), LocalDateTime.of(YEAR, MONTH_05, 5, 10, 0));

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

        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getFrom(), LocalDateTime.of(YEAR, MONTH_05, 5, 15, 0));
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
        Assert.assertEquals(freeSlotsWhenOnlyOneAppointmentPerDay.get(1).getTo(), LocalDateTime.of(YEAR, MONTH_05, 5, 15, 0));
    }

    private void createAppointment(List<AppointmentSlot> appointmentSlotList,
                                   int dayFrom, int hourFrom, int minuteFrom,
                                   int dayTo, int hourTo, int minuteTo) {
        LocalDateTime from = LocalDateTime.of(YEAR, MONTH_05, dayFrom, hourFrom, minuteFrom);
        LocalDateTime to = LocalDateTime.of(YEAR, MONTH_05, dayTo, hourTo, minuteTo);
        appointmentSlotList.add(new AppointmentSlot(from, to));
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

        LocalDateTime app1From = LocalDateTime.of(YEAR, 5, 5, 9, 0);
        LocalDateTime app1To = LocalDateTime.of(YEAR, 5, 5, 10, 0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        LocalDateTime app2From = LocalDateTime.of(YEAR, 5, 5, 14, 30);
        LocalDateTime app2To = LocalDateTime.of(YEAR, 5, 5, 15, 0);
        appointmentSlotList.add(new AppointmentSlot(app2From, app2To));

        LocalDateTime app3From = LocalDateTime.of(YEAR, 5, 5, 16, 30);
        LocalDateTime app3To = LocalDateTime.of(YEAR, 5, 5, 17, 0);
        appointmentSlotList.add(new AppointmentSlot(app3From, app3To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 5);
    }

    @Test
    public void searchInRangeOnMultipleDays() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(YEAR, 5, 5, 9, 0);
        LocalDateTime app1To = LocalDateTime.of(YEAR, 5, 5, 10, 0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        LocalDateTime app2From = LocalDateTime.of(YEAR, 5, 5, 14, 30);
        LocalDateTime app2To = LocalDateTime.of(YEAR, 5, 5, 15, 0);
        appointmentSlotList.add(new AppointmentSlot(app2From, app2To));

        LocalDateTime app3From = LocalDateTime.of(YEAR, 5, 5, 16, 30);
        LocalDateTime app3To = LocalDateTime.of(YEAR, 5, 5, 17, 0);
        appointmentSlotList.add(new AppointmentSlot(app3From, app3To));

        LocalDateTime app4From = LocalDateTime.of(YEAR, 5, 6, 13, 30);
        LocalDateTime app4To = LocalDateTime.of(YEAR, 5, 6, 14, 30);
        appointmentSlotList.add(new AppointmentSlot(app4From, app4To));

        LocalDateTime app5From = LocalDateTime.of(YEAR, 5, 6, 16, 30);
        LocalDateTime app5To = LocalDateTime.of(YEAR, 5, 6, 17, 0);
        appointmentSlotList.add(new AppointmentSlot(app5From, app5To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-06", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 7);
    }

    @Test
    public void searchInRangeWhenAppointmentFromStartOfDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(YEAR, 5, 5, 8, 0);
        LocalDateTime app1To = LocalDateTime.of(YEAR, 5, 5, 10, 0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 2);
    }

    @Test
    public void searchInRangeWhenAppointmentIsLastOnTheDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(YEAR, 5, 5, 15, 0);
        LocalDateTime app1To = LocalDateTime.of(YEAR, 5, 5, 18, 0); // use end of day
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 2);
    }

}
