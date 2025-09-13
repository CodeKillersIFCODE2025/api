package br.com.codekillers.zelo.DTO.Mapper;

import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.Domain.Elderly;

public class ElderlyMapper {
    public static Elderly toEntity(ElderlyRequest request) {
        return new Elderly(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
    }
}
