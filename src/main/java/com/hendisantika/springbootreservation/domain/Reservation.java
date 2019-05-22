package com.hendisantika.springbootreservation.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-20
 * Time: 04:28
 */
@Entity
@Data
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "reserved_date"), @JoinColumn(name = "room_id")})
    private ReservableRoom reservableRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public boolean overlap(Reservation target) {

        //Is the room and date the same? Is it another?
        if (!Objects.equals(reservableRoom.getReservableRoomId(), target.reservableRoom.getReservableRoomId())) {
            //Not duplicate
            return false;
        }
        //Is the time exactly the same?
        if (startTime.equals(target.startTime) && endTime.equals(target.endTime)) {
            //重複である
            return true;
        }

        //Are the times even overlapping?
        return target.endTime.isAfter(startTime) && endTime.isAfter(target.startTime);

    }


}
