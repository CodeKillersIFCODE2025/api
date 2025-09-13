package br.com.codekillers.zelo.DTO.Mapper;

import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.DTO.Response.ElderlyResponse;
import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Utils.Date;
import com.google.cloud.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.codekillers.zelo.Utils.Cryptography.encryptPassword;

public class ElderlyMapper {
    public static Elderly toEntity(ElderlyRequest request) {
        return new Elderly(
                request.getName(),
                request.getEmail(),
                encryptPassword(request.getPassword())
        );
    }

    public static ElderlyResponse toResponse(Elderly elderly) {
        return new ElderlyResponse(
                elderly.getId(),
                elderly.getName(),
                elderly.getEmail(),
                Date.formatFirestoreTimestamp(elderly.getLastCheckIn()),
                isLastCheckInToday(elderly.getLastCheckIn())
        );
    }

    private static boolean isLastCheckInToday(Timestamp lastCheckIn) {
        if (lastCheckIn == null) {
            return false;
        }

        LocalDate dataCheckIn = lastCheckIn.toSqlTimestamp().toLocalDateTime().toLocalDate();
        LocalDate hoje = LocalDate.now();

        return dataCheckIn.isEqual(hoje);
    }
}
