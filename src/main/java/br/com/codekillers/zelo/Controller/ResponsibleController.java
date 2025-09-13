package br.com.codekillers.zelo.Controller;

import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.Service.ResponsibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;

@Controller
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
