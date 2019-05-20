package com.hendisantika.springbootreservation.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:26
 */
public class UnavailableReservationException extends RuntimeException {

    public UnavailableReservationException(String message) {
        super(message);
    }

}
