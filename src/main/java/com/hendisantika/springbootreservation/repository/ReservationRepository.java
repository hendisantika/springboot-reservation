package com.hendisantika.springbootreservation.repository;

import com.hendisantika.springbootreservation.domain.ReservableRoomId;
import com.hendisantika.springbootreservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:19
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByReservableRoomReservableRoomIdOrderByStartTimeAsc(ReservableRoomId reservableId);


}