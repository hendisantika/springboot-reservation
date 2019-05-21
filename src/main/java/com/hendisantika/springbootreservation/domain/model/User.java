package com.hendisantika.springbootreservation.domain.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-20
 * Time: 04:28
 */
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private String userId;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

}
