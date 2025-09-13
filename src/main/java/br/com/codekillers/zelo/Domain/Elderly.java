package br.com.codekillers.zelo.Domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @ToString @Builder
public class Elderly extends User{
    private List<Task> agenda;
    private LocalDateTime lastCheckIn;
}
