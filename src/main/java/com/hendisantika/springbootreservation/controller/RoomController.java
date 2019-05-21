package com.hendisantika.springbootreservation.controller;

import com.hendisantika.springbootreservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
