package com.hendisantika.springbootreservation.annotation;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-22
 * Time: 05:23
 */
@EndTimeMustBeAfterStartTime(message = "The end time must be later than the start time")
public class ReservationForm implements Serializable {

    @NotNull(message = "Required")
    @ThirtyMinutesUnit(message = "Please enter in 30 minutes")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "Required")
    @ThirtyMinutesUnit(message = "Please enter in 30 minutes")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


}
