package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ResponsibleService responsibleService;

    public String login(LoginRequest request) {


        return "";
    }
}
