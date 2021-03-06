package appointment;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

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

}
