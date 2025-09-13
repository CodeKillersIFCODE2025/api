package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.Service.ElderlyService;
import br.com.codekillers.zelo.Service.ResponsibleService;
import br.com.codekillers.zelo.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/elderly")
public class ElderlyController {

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createElderly(@RequestBody ElderlyRequest request,
                              @AuthenticationPrincipal UserDetails userDetails) {
        elderlyService.createElderly(request, userDetails);
    }

    @GetMapping("/day-tasks")
    public List<TaskResponse> listElderyTasksForTheDay(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.listTasksForTheDay(userDetails.getUsername());
    }
}
