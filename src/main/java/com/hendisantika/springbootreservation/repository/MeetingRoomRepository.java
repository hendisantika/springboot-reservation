package com.hendisantika.springbootreservation.repository;

import com.hendisantika.springbootreservation.domain.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:20
 */
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Integer> {
}