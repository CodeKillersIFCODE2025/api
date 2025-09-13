package br.com.codekillers.zelo.DTO.Mapper;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.Domain.Responsible;

public class ResponsibleMapper {
    public static Responsible toEntity(ResponsibleRequest request) {
        return new Responsible(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getAddress()
        );
    }
}
