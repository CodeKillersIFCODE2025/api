package br.com.codekillers.zelo.Domain;

import lombok.*;

@Getter @ToString @Builder
public class Task {
    private String id;
    private String description;
    private boolean isRepeated;
    private int frequency;
    private FrequencyUnit frequencyUnit;
}
