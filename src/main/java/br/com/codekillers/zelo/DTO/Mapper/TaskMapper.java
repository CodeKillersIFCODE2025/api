package br.com.codekillers.zelo.DTO.Mapper;

import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.Domain.FrequencyUnit;
import br.com.codekillers.zelo.Domain.Task;

public class TaskMapper {
    public static Task toEntity(TaskRequest taskRequest) {
        return Task.builder()
                .description(taskRequest.getDescription())
                .isRepeated(taskRequest.isRepeated())
                .frequency(taskRequest.getFrequency())
                .frequencyUnit(FrequencyUnit.valueOf(taskRequest.getFrequencyUnit().toUpperCase()))
                .build();
    }
}
