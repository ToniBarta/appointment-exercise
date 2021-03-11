package appointment;

import org.junit.Assert;
import org.junit.Test;

public class InputValidationTest {

    @Test
    public void validateDateInputWhenCorrect() {
       String date = "2021-02-10";
       Assert.assertTrue(InputValidation.validateDateInput(date));
    }

    @Test
    public void validateDateInputWhenIncorrect() {
        String date = "2021/02/10";
        Assert.assertFalse(InputValidation.validateDateInput(date));
    }

    @Test
    public void validateHourInputWhenCorrect() {
        String hour = "10:30";
        Assert.assertTrue(InputValidation.validateHourInput(hour));
    }

    @Test
    public void validateHourInputWhenIncorrect() {
        String hour = "10-30";
        Assert.assertFalse(InputValidation.validateHourInput(hour));
    }
}
