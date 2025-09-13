package br.com.codekillers.zelo.DTO.Response;

import lombok.*;

@Getter @Setter
public class ResponsibleResponse extends UserResponse{
    private String phone;
    private String address;
    private ElderlyResponse elderly;

    public ResponsibleResponse(String id, String name, String email, String phone, String address, ElderlyResponse elderly) {
        super(id, name, email);
        this.phone = phone;
        this.address = address;
        this.elderly = elderly;
    }
}
