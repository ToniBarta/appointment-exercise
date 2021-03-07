package appointment;

import domain.AppointmentSlot;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static appointment.AppoinmentConsts.END_OF_LUNCH;
import static appointment.AppoinmentConsts.START_OF_LUNCH;

public class RangeSearchImplTest {

    @Test
    public void isLunchIfTimeBeforeLunch() {
        LocalTime beforeLunch = LocalTime.of(9,0);
        Assert.assertFalse(RangeSearchImpl.isLunchTime(beforeLunch));
    }

    @Test
    public void isLunchIfTimeIsStartOfLunch() {
        LocalTime startOfLunch = START_OF_LUNCH;
        Assert.assertTrue(RangeSearchImpl.isLunchTime(startOfLunch));
    }

    @Test
    public void isLunchIfTimeIsBetweenLunch() {
        LocalTime betweenLunch = LocalTime.of(12, 30);

        Assert.assertTrue(START_OF_LUNCH.isBefore(betweenLunch));
        Assert.assertTrue(END_OF_LUNCH.isAfter(betweenLunch));
        Assert.assertTrue(RangeSearchImpl.isLunchTime(betweenLunch));
    }

    @Test
    public void isLunchIfTimeIsEndOfLunch() {
        LocalTime endOfLunch = END_OF_LUNCH;
        Assert.assertFalse(RangeSearchImpl.isLunchTime(endOfLunch));
    }

    @Test
    public void isLunchIfTimeAfterLunch() {
        LocalTime afterLunch = LocalTime.of(16,0);
        Assert.assertFalse(RangeSearchImpl.isLunchTime(afterLunch));
    }

    @Test
    public void whenAppointmentIsBeforeSearchRange() {
        LocalDate appointmentBeforeSearchRange = LocalDate.of(2021, 5, 1);
        LocalDate fromSearchRange = LocalDate.of(2021, 5, 2);
        LocalDate toSearchRange = LocalDate.of(2021, 5, 10);

        Assert.assertFalse(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentBeforeSearchRange));
    }

    @Test
    public void whenAppointmentEqualsWithFromSearchRange() {
        LocalDate appointmentSameAsFromRange = LocalDate.of(2021, 5, 1);
        LocalDate fromSearchRange = LocalDate.of(2021, 5, 1);
        LocalDate toSearchRange = LocalDate.of(2021, 5, 10);

        Assert.assertTrue(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentSameAsFromRange));
    }

    @Test
    public void whenAppointmentIsBetweenSearchRange() {
        LocalDate appointmentInBetweenSearchRange = LocalDate.of(2021, 5, 5);
        LocalDate fromSearchRange = LocalDate.of(2021, 5, 1);
        LocalDate toSearchRange = LocalDate.of(2021, 5, 10);

        Assert.assertTrue(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentInBetweenSearchRange));
    }

    @Test
    public void whenAppointmentIsEqualToToSearchRange() {
        LocalDate appointmentSameAsToRange = LocalDate.of(2021, 5, 5);
        LocalDate fromSearchRange = LocalDate.of(2021, 5, 1);
        LocalDate toSearchRange = LocalDate.of(2021, 5, 5);

        Assert.assertTrue(RangeSearchImpl.isBetweenSearchRange(fromSearchRange, toSearchRange, appointmentSameAsToRange));
    }

    @Test
    public void whenAppointmentIsAfterSearchRange() {
        LocalDate appointmentAfterSearchRange = LocalDate.of(2021, 5, 5);
        LocalDate fromSearchRange = LocalDate.of(2021, 5, 1);
        LocalDate toSearchRange = LocalDate.of(2021, 5, 4);

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

        LocalDate expectedDate = LocalDate.of(2021, 1, 1);
        Assert.assertEquals(localDate, expectedDate);
    }

    @Test
    public void searchInRangeOnTheSameDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(2021, 5, 5, 9,0);
        LocalDateTime app1To = LocalDateTime.of(2021, 5, 5, 10,0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        LocalDateTime app2From = LocalDateTime.of(2021, 5, 5, 14,30);
        LocalDateTime app2To = LocalDateTime.of(2021, 5, 5, 15,0);
        appointmentSlotList.add(new AppointmentSlot(app2From, app2To));

        LocalDateTime app3From = LocalDateTime.of(2021, 5, 5, 16,30);
        LocalDateTime app3To = LocalDateTime.of(2021, 5, 5, 17,0);
        appointmentSlotList.add(new AppointmentSlot(app3From, app3To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 4);
    }

    @Test
    public void searchInRangeOnMultipleDays() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(2021, 5, 5, 9,0);
        LocalDateTime app1To = LocalDateTime.of(2021, 5, 5, 10,0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        LocalDateTime app2From = LocalDateTime.of(2021, 5, 5, 14,30);
        LocalDateTime app2To = LocalDateTime.of(2021, 5, 5, 15,0);
        appointmentSlotList.add(new AppointmentSlot(app2From, app2To));

        LocalDateTime app3From = LocalDateTime.of(2021, 5, 5, 16,30);
        LocalDateTime app3To = LocalDateTime.of(2021, 5, 5, 17,0);
        appointmentSlotList.add(new AppointmentSlot(app3From, app3To));

        LocalDateTime app4From = LocalDateTime.of(2021, 5, 6, 13,30);
        LocalDateTime app4To = LocalDateTime.of(2021, 5, 6, 14,30);
        appointmentSlotList.add(new AppointmentSlot(app4From, app4To));

        LocalDateTime app5From = LocalDateTime.of(2021, 5, 6, 16,30);
        LocalDateTime app5To = LocalDateTime.of(2021, 5, 6, 17,0);
        appointmentSlotList.add(new AppointmentSlot(app5From, app5To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-06", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 7);
    }

    @Test
    public void searchInRangeWhenAppointmentFromStartOfDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(2021, 5, 5, 8,0);
        LocalDateTime app1To = LocalDateTime.of(2021, 5, 5, 10,0);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 1);
    }

    @Test
    public void searchInRangeWhenAppointmentIsLastOnTheDay() {
        List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

        LocalDateTime app1From = LocalDateTime.of(2021, 5, 5, 15,0);
        LocalDateTime app1To = LocalDateTime.of(2021, 5, 5, 18,0); // use end of day
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));

        List<AppointmentSlot> freeSlots = RangeSearchImpl.searchInRange2("2021-05-05", "2021-05-05", appointmentSlotList);
        Assert.assertEquals(freeSlots.size(), 1);
    }

}
