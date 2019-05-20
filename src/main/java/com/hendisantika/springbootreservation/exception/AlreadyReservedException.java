package com.hendisantika.springbootreservation.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:25
 */
public class AlreadyReservedException extends RuntimeException {

    public AlreadyReservedException(String message) {
        super(message);
    }
}