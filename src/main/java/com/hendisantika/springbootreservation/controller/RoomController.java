package com.hendisantika.springbootreservation.controller;

import com.hendisantika.springbootreservation.domain.ReservableRoom;
import com.hendisantika.springbootreservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping
    String listRooms(Model model) {
        LocalDate today = LocalDate.now();
        //Return today's reservation list
        List<ReservableRoom> rooms = roomService.findReservableRooms(today);

        model.addAttribute("date", today);
        model.addAttribute("rooms", rooms);

        return "room/listRooms";

    }

    /**
     * Return of reservation list for specific day(/rooms/{date})
     *
     * @param model
     * @return
     */
    @GetMapping(path = "{date}")
    String listRooms(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date, Model model) {
        LocalDate today = LocalDate.now();
        //Return today's reservation list
        List<ReservableRoom> rooms = roomService.findReservableRooms(date);

        model.addAttribute("rooms", rooms);

        return "reservation/reserveForm";

    }


}
