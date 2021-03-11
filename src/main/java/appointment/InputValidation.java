package appointment;

public class InputValidation {

    //also validation if the toInput is larger than the fromInput

    // this method can have a lot of other ways of testing the input. E.G if the month or date is correct, but
    // will not add this at this time.
    public static boolean validateDateInput(String date) {
        String[] regex = date.split("-");
        if (regex.length != 3) {
            System.out.println("The date has to have this format yyyy-mm-dd. eg. 2021-03-01");
            return false;
        }
        return true;
    }

    // this method can have a lot of other ways of testing the input. E.G if the month or date is correct, but
    // will not add this at this time.
    public static boolean validateHourInput(String hour) {
        String[] regex = hour.split(":");
        if (regex.length != 2) {
            System.out.println("The hour has to have this format hh:mm eg. 09:05");
            return false;
        }
        return true;
    }
}
