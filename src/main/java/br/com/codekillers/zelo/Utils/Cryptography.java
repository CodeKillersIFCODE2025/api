package br.com.codekillers.zelo.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Cryptography {
    public static String encryptPassword(String plainPassword){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainPassword);
    }

    private static boolean matchPassword(String plainPassword, String encryptedPassword){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }
}
