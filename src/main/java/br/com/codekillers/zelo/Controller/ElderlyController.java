package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.Service.ElderlyService;
import br.com.codekillers.zelo.Service.ResponsibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/elderly")
public class ElderlyController {

    @Autowired
    private ElderlyService elderlyService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createElderly(@RequestBody ElderlyRequest request,
                              @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername());
        try {
            elderlyService.createElderly(request, userDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
