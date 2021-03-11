package domain;

import java.time.LocalDateTime;

public class AppointmentSlot {

    private LocalDateTime from;
    private LocalDateTime to;

    public AppointmentSlot(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }


    @Override
    public String toString() {
        return "AppointmentSlot{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
