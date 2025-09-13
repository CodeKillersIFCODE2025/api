package br.com.codekillers.zelo.DTO.Mapper;

import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.Domain.FrequencyUnit;
import br.com.codekillers.zelo.Domain.Task;
import br.com.codekillers.zelo.Utils.Date;
import com.google.cloud.Timestamp;
import com.google.type.DateTime;

import java.time.LocalDateTime;

public class TaskMapper {
    public static Task toEntity(TaskRequest taskRequest) {
        return Task.builder()
                .description(taskRequest.getDescription())
                .isRepeated(taskRequest.isRepeated())
                .frequencyUnit(FrequencyUnit.valueOf(taskRequest.getFrequencyUnit().toUpperCase()))
                .nextActionDue(Timestamp.of(taskRequest.getStartDate()))
                .build();
    }

    public static TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .date(Date.formatFirestoreTimestamp(task.getNextActionDue()))
                .build();
    }
}
