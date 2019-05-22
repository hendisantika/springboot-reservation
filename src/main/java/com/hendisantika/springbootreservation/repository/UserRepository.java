package com.hendisantika.springbootreservation.repository;

import com.hendisantika.springbootreservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-reservation
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-05-21
 * Time: 05:23
 */
public interface UserRepository extends JpaRepository<User, String> {
}