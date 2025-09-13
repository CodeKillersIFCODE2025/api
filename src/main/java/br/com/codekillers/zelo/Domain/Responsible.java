package br.com.codekillers.zelo.Domain;

import lombok.*;

@Getter @Setter @ToString @Builder
public class Responsible extends User{
    private String phone;
    private String address;
    private Elderly elderly;

    public Responsible(String name, String email, String password, String phone, String address) {
        super(name, email, password);
        this.phone = phone;
        this.address = address;
    }
}
