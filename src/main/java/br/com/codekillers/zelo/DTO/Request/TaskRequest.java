package br.com.codekillers.zelo.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class TaskRequest {
    private String description;
    private Timestamp startDate;
    private String frequencyUnit;
    private boolean repeated;
}
