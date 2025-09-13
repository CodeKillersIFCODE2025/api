package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.Service.ResponsibleService;
import br.com.codekillers.zelo.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/responsibles")
public class ResponsibleController {

    @Autowired
    private ResponsibleService responsibleService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createResponsible(@RequestBody ResponsibleRequest request) {
        try {
            responsibleService.createResponsible(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/tasks")
    @ResponseStatus(CREATED)
    public void createTask(@RequestBody TaskRequest taskRequest,
                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            taskService.addTaskForElderly(taskRequest, userDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/tasks")
    public HashMap<DayOfWeek, List<TaskResponse>> listWeekTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.listTasksForTheWeek(userDetails.getUsername());
    }
}
