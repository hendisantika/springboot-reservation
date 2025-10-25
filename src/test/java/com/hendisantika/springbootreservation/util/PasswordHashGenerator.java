package com.hendisantika.springbootreservation.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password1 = "53cret";
        String hash1 = encoder.encode(password1);

        System.out.println("Password: " + password1);
        System.out.println("BCrypt Hash: " + hash1);
        System.out.println();

        // Verify the hash works
        boolean matches = encoder.matches(password1, hash1);
        System.out.println("Hash verification: " + (matches ? "✅ SUCCESS" : "❌ FAILED"));
    }
}
