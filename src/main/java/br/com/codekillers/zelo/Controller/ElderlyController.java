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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

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
    public ResponseEntity<Object> createElderly(@RequestBody ElderlyRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String response = elderlyService.createElderly(request, userDetails);

        return response != null
                ? new ResponseEntity<>(response, BAD_REQUEST)
                : new ResponseEntity<>("Idoso cadastrado com sucesso!", CREATED);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getElderly(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.authenticateUser(userDetails.getUsername());

        return user instanceof ElderlyResponse
                ? new ResponseEntity<>(user, OK)
                : new ResponseEntity<>(((ResponsibleResponse) user).getElderly(), OK);
    }

    @GetMapping("/tasks/today")
    public List<TaskResponse> listElderyTasksForTheDay(@AuthenticationPrincipal UserDetails userDetails) {
        return taskService.listTasksForTheDay(userDetails.getUsername());
    }

    @PutMapping("/tasks/{taskID}")
    public ResponseEntity<String> completeTaskForTheDay(@PathVariable String taskID){
        String response = taskService.completeTaskForTheDay(taskID);

        return response == null
                ? new ResponseEntity<>("Task completa!", OK)
                : new ResponseEntity<>(response, BAD_REQUEST);
    }

    @PutMapping("/checkin")
    public ResponseEntity<ElderlyResponse> dailyCheckIn(@AuthenticationPrincipal UserDetails userDetails){
        ElderlyResponse response = elderlyService.dailyCheckIn(userDetails.getUsername());

        return new ResponseEntity<>(response, OK);
    }
}
