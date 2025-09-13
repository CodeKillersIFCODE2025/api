package br.com.codekillers.zelo.Domain;

import com.google.cloud.Timestamp;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString @Builder
public class Task {
    private String id;
    private String description;
    private boolean isRepeated;
    private int frequency;
    private FrequencyUnit frequencyUnit;
    private String userId;
    private List<Timestamp> nextActionDue;
}
