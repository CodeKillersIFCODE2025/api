package br.com.codekillers.zelo.Domain;

import lombok.*;

@Getter @ToString
public abstract class User {
    private String id;
    private String name;
    private String email;
    @Setter
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
