package com.hendisantika.springbootreservation.service;

import com.hendisantika.springbootreservation.domain.model.ReservableRoom;
import com.hendisantika.springbootreservation.domain.model.ReservableRoomId;
import com.hendisantika.springbootreservation.domain.model.Reservation;
import com.hendisantika.springbootreservation.exception.AlreadyReservedException;
import com.hendisantika.springbootreservation.exception.UnavailableReservationException;
import com.hendisantika.springbootreservation.repository.ReservableRoomRepository;
import com.hendisantika.springbootreservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:27
 */
@Service
@Transactional
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservableRoomRepository reservableRoomRepository;

    /**
     * Return a list of reservations.
     *
     * @param reservableRoomId
     * @return
     */
    public List<Reservation> findReservations(ReservableRoomId reservableRoomId) {

        return reservationRepository.findByReservableRoomReservableRoomIdOrderByStartTimeAsc(reservableRoomId);

    }

    /**
     * Reserve
     *
     * @param reservation
     * @return
     */
    public Reservation reserve(Reservation reservation) {

        //Get room and date from booking
        ReservableRoomId reservableRoomId = reservation.getReservableRoom().getReservableRoomId();

        //Get availability from a room and date
        Optional<ReservableRoom> reservableData = reservableRoomRepository.findById(reservableRoomId);
        ReservableRoom reservable = reservableData.get();

        if (reservable == null) {
            //Throw an exception because it is not available
            throw new UnavailableReservationException("Combination of input date and room can not be reserved.");
        }

        //Are there duplicate bookings?
        boolean overlap = reservationRepository.findByReservableRoomReservableRoomIdOrderByStartTimeAsc(reservableRoomId)
                .stream()
                .anyMatch(x -> x.overlap(reservation));

        if (overlap) {
            //exception
            throw new AlreadyReservedException("The time of entry is already reserved.");
        }

        //Preservation
        reservationRepository.save(reservation);

        return reservation;

    }

    @PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
    public void cancel(@Param("reservation") Reservation reservation) {

        reservationRepository.delete(reservation);

    }

}
