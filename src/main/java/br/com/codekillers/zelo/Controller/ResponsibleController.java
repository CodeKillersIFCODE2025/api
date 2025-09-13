package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.DTO.Response.ResponsibleResponse;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.Service.ResponsibleService;
import br.com.codekillers.zelo.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/responsibles")
public class ResponsibleController {

    @Autowired
    private ResponsibleService responsibleService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Object> createResponsible(@RequestBody ResponsibleRequest request) {
        String response = responsibleService.createResponsible(request);

        return response != null
                ? new ResponseEntity<>(response, BAD_REQUEST)
                : new ResponseEntity<>("Responsável criado com sucesso!", CREATED);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Object> createTask(@RequestBody TaskRequest taskRequest,
                                 @AuthenticationPrincipal UserDetails userDetails) {

            String response = taskService.addTaskForElderly(taskRequest, userDetails);

            return response != null
                    ? new ResponseEntity<>(response, BAD_REQUEST)
                    : new ResponseEntity<>("Tarefa criada com sucesso!", CREATED);
    }

    @GetMapping("/tasks")
    public HashMap<DayOfWeek, List<TaskResponse>> listWeekTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.listTasksForTheWeek(userDetails.getUsername());
    }

    @GetMapping
    public ResponseEntity<Object> getResponsible(@AuthenticationPrincipal UserDetails userDetails) {
        Object response = responsibleService.getResponsibleByElderly(userDetails.getUsername());

        return response instanceof ResponsibleResponse
                ?  new ResponseEntity<>(response, OK)
                : new ResponseEntity<>(response, BAD_REQUEST);
    }
}
