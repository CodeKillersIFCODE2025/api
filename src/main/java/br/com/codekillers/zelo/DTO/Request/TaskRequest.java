package br.com.codekillers.zelo.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Getter @Setter
public class TaskRequest {
    private String description;
    private boolean isRepeated;
    private Timestamp startDate;
    private int frequency;
    private String frequencyUnit;
}
