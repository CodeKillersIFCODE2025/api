package br.com.codekillers.zelo.DTO.Mapper;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.DTO.Response.ResponsibleResponse;
import br.com.codekillers.zelo.DTO.Response.UserResponse;
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

    public static ResponsibleResponse toResponse(Responsible responsible) {
        return new ResponsibleResponse(
                responsible.getId(),
                responsible.getName(),
                responsible.getEmail(),
                responsible.getPhone(),
                responsible.getAddress(),
                responsible.getElderly() != null
                        ? ElderlyMapper.toResponse(responsible.getElderly())
                        : null
        );
    }
}
