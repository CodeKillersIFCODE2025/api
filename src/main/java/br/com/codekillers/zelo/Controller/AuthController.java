package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Response.UserResponse;
import br.com.codekillers.zelo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.authenticateUser(userDetails.getUsername());

        return response != null ? ResponseEntity.ok(response) :
                ResponseEntity.notFound().build();
    }
}
