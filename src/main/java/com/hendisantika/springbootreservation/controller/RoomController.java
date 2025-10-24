package com.hendisantika.springbootreservation.controller;

import com.hendisantika.springbootreservation.domain.ReservableRoom;
import com.hendisantika.springbootreservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing meeting room listings
 *
 * Uses constructor injection via Lombok's @RequiredArgsConstructor for better testability
 * and immutability of dependencies.
 *
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
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

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
     * @param date The specific date to show available rooms for
     * @param model Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping(path = "{date}")
    String listRooms(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date, Model model) {
        // Get list of reservable rooms for the specified date
        List<ReservableRoom> rooms = roomService.findReservableRooms(date);

        model.addAttribute("date", date);
        model.addAttribute("rooms", rooms);

        return "room/listRooms";
    }


}
