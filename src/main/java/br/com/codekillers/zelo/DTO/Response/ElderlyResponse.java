package br.com.codekillers.zelo.DTO.Response;

import com.google.cloud.Timestamp;

public class ElderlyResponse extends UserResponse {
    private Timestamp lastCheckIn;
    private boolean isTodayCheckInDone;

    public ElderlyResponse(String id, String name, String email, Timestamp lastCheckIn, boolean isTodayCheckInDone) {
        super(id, name, email);
        this.lastCheckIn = lastCheckIn;
        this.isTodayCheckInDone = isTodayCheckInDone;
    }
}
