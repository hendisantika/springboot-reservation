package com.hendisantika.springbootreservation.controller;

import com.hendisantika.springbootreservation.domain.model.ReservableRoom;
import com.hendisantika.springbootreservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-22
 * Time: 05:19
 */
@Controller
@RequestMapping("rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    /**
     * Return of today's reservation list(/rooms)
     *
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    String listRooms(Model model) {
        LocalDate today = LocalDate.now();
        //Return today's reservation list
        List<ReservableRoom> rooms = roomService.findReservableRooms(today);

        model.addAttribute("date", today);
        model.addAttribute("rooms", rooms);

        return "room/listRooms";

    }


}
