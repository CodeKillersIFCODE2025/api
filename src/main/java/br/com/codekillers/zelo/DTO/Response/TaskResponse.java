package br.com.codekillers.zelo.DTO.Response;

import br.com.codekillers.zelo.Domain.FrequencyUnit;
import com.google.cloud.Timestamp;
import com.google.type.DateTime;
import lombok.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class TaskResponse {
    private String id;
    private String description;
    private String date;
}
