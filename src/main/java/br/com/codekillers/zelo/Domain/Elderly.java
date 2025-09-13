package br.com.codekillers.zelo.Domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString
public class Elderly extends User{
    private List<Task> agenda;
    private LocalDateTime lastCheckIn;

    public Elderly(String name, String email, String password) {
        super(name, email, password);
        agenda = new ArrayList<>();
        lastCheckIn = LocalDateTime.now();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getUsername(){
        return super.getEmail();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
