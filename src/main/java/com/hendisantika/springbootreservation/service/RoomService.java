package com.hendisantika.springbootreservation.service;

import com.hendisantika.springbootreservation.domain.MeetingRoom;
import com.hendisantika.springbootreservation.domain.ReservableRoom;
import com.hendisantika.springbootreservation.repository.MeetingRoomRepository;
import com.hendisantika.springbootreservation.repository.ReservableRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:32
 */
@Service
@Transactional
public class RoomService {

    @Autowired
    ReservableRoomRepository reservableRoomRepository;

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    public List<ReservableRoom> findReservableRooms(LocalDate date) {

        return reservableRoomRepository.findByReservableRoomIdReservedDateOrderByReservableRoomIdRoomIdAsc(date);

    }


    public Optional<MeetingRoom> findMeetingRoom(Long roomId) {

        return meetingRoomRepository.findById(Math.toIntExact(roomId));
    }

}