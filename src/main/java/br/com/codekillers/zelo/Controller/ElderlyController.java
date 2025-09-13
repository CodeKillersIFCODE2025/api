package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.DTO.Response.ElderlyResponse;
import br.com.codekillers.zelo.DTO.Response.ResponsibleResponse;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.DTO.Response.UserResponse;
import br.com.codekillers.zelo.Service.ElderlyService;
import br.com.codekillers.zelo.Service.TaskService;
import br.com.codekillers.zelo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/elderly")
public class ElderlyController {

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public HttpStatus createElderly(@RequestBody ElderlyRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        return elderlyService.createElderly(request, userDetails) != null
                ? BAD_REQUEST
                : CREATED;
    }

    @GetMapping
    public UserResponse getElderly(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.authenticateUser(userDetails.getUsername());

        return user instanceof ElderlyResponse
                ? user
                : ((ResponsibleResponse) user).getElderly();
    }

    @GetMapping("/tasks/today")
    public List<TaskResponse> listElderyTasksForTheDay(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.listTasksForTheDay(userDetails.getUsername());
    }

    @PutMapping("/tasks/{taskID}")
    public boolean completeTaskForTheDay(@PathVariable String taskID){
        return taskService.completeTaskForTheDay(taskID);
    }

    @PutMapping("/checkin")
    public ElderlyResponse dailyCheckIn(@AuthenticationPrincipal UserDetails userDetails){
        return elderlyService.dailyCheckIn(userDetails.getUsername());
    }
}
