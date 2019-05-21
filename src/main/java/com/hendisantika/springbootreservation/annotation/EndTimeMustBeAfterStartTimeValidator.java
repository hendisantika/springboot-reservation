package com.hendisantika.springbootreservation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-22
 * Time: 05:23
 */
public class EndTimeMustBeAfterStartTimeValidator implements ConstraintValidator<EndTimeMustBeAfterStartTime, ReservationForm> {

    private String message;

    @Override
    public void initialize(EndTimeMustBeAfterStartTime constraintAnnotation) {
        message = constraintAnnotation.message();


    }

    @Override
    public boolean isValid(ReservationForm value, ConstraintValidatorContext context) {
        if (value.getStartTime() == null || value.getEndTime() == null) {
            return true;
        }
        boolean isEndTimeMustBeAfterStartTime = value.getEndTime()
                .isAfter(value.getStartTime());
        if (!isEndTimeMustBeAfterStartTime) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("endTime").addConstraintViolation();

        }
        return isEndTimeMustBeAfterStartTime;
    }


}