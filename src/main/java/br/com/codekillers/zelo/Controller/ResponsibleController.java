package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.DTO.Response.ResponsibleResponse;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.DTO.Response.UserResponse;
import br.com.codekillers.zelo.Service.ResponsibleService;
import br.com.codekillers.zelo.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/responsibles")
public class ResponsibleController {

    @Autowired
    private ResponsibleService responsibleService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public HttpStatus createResponsible(@RequestBody ResponsibleRequest request) {
        String response = responsibleService.createResponsible(request);

        return response != null
                ? BAD_REQUEST
                : CREATED;
    }

    @PostMapping("/tasks")
    public HttpStatus createTask(@RequestBody TaskRequest taskRequest,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            taskService.addTaskForElderly(taskRequest, userDetails);
            return CREATED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BAD_REQUEST;
    }

    @GetMapping("/tasks")
    public HashMap<DayOfWeek, List<TaskResponse>> listWeekTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.listTasksForTheWeek(userDetails.getUsername());
    }

    @GetMapping
    public ResponsibleResponse getResponsible(@AuthenticationPrincipal UserDetails userDetails) {
        return responsibleService.getResponsibleByElderly(userDetails.getUsername());
    }


}
