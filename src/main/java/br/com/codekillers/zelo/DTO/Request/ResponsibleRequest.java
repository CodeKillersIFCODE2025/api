package br.com.codekillers.zelo.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponsibleRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}
