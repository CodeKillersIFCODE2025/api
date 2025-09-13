package br.com.codekillers.zelo.Domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class Elderly extends User{
    private List<Task> agenda;
    private LocalDateTime lastCheckIn;

    public Elderly(String name, String email, String password) {
        super(name, email, password);
        agenda = new ArrayList<>();
        lastCheckIn = LocalDateTime.now();
    }
}
