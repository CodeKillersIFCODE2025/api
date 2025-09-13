package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.Service.ResponsibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/responsibles")
public class ResponsibleController {

    @Autowired
    private ResponsibleService responsibleService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void addNewUser(@RequestBody ResponsibleRequest request) {
        try {
            responsibleService.createResponsible(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
