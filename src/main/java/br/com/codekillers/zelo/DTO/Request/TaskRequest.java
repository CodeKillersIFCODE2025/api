package br.com.codekillers.zelo.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskRequest {
    private String description;
    private boolean isRepeated;
    private int frequency;
    private String frequencyUnit;
}
