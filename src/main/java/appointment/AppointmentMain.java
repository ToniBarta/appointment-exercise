package appointment;

import domain.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentMain {

    public static List<AppointmentSlot> appointmentSlotList = new ArrayList<>();

    public static void main(String[] args) {

        LocalDateTime app1From = LocalDateTime.of(2021, 2, 4, 10,0);
        LocalDateTime app1To = LocalDateTime.of(2021, 2, 4, 10,30);
        appointmentSlotList.add(new AppointmentSlot(app1From, app1To));


        LocalDateTime app2From = LocalDateTime.of(2021, 2, 5, 11,0);
        LocalDateTime app2To = LocalDateTime.of(2021, 2, 5, 11,30);
        appointmentSlotList.add(new AppointmentSlot(app2From, app2To));


        LocalDateTime app3From = LocalDateTime.of(2021, 2, 5, 15,0);
        LocalDateTime app3To = LocalDateTime.of(2021, 2, 5, 15,30);
        appointmentSlotList.add(new AppointmentSlot(app3From, app3To));


        LocalDateTime app4From = LocalDateTime.of(2021, 2, 6, 10,0);
        LocalDateTime app4To = LocalDateTime.of(2021, 2, 6, 10,30);
        appointmentSlotList.add(new AppointmentSlot(app4From, app4To));


        LocalDateTime app5From = LocalDateTime.of(2021, 2, 6, 11,0);
        LocalDateTime app5To = LocalDateTime.of(2021, 2, 6, 11,30);
        appointmentSlotList.add(new AppointmentSlot(app5From, app5To));


        LocalDateTime app6From = LocalDateTime.of(2021, 2, 6, 17,30);
        LocalDateTime app6To = LocalDateTime.of(2021, 2, 6, 18,0);
        appointmentSlotList.add(new AppointmentSlot(app6From, app6To));

//        searchInRange("2021-02-04", "2021-02-05");
    }

}


//
//const weeklyAppointments = [
//        { from: "2021-01-04T10:00:00", to: "2021-01-04T10:30:00" },
//        { from: "2021-01-05T11:00:00", to: "2021-01-05T11:30:00" },
//        { from: "2021-01-05T15:30:00", to: "2021-01-05T16:30:00" },
//        { from: "2021-01-06T10:00:00", to: "2021-01-06T10:30:00" },
//        { from: "2021-01-06T11:00:00", to: "2021-01-06T12:30:00" },
//        { from: "2021-01-06T17:30:00", to: "2021-01-06T18:00:00" },
//        ];
