package br.com.codekillers.zelo.DTO.Response;

import lombok.*;


@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class UserResponse {
    private String id;
    private String name;
    private String email;
}
