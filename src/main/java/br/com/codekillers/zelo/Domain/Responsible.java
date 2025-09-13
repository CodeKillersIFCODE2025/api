package br.com.codekillers.zelo.Domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString
public class Responsible extends User{
    private String phone;
    private String address;
    private Elderly elderly;

    public Responsible(String name, String email, String password, String phone, String address) {
        super(name, email, password);
        this.phone = phone;
        this.address = address;
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
