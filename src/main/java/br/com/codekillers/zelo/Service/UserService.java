package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.ResponsibleMapper;
import br.com.codekillers.zelo.DTO.Response.UserResponse;
import br.com.codekillers.zelo.Domain.Responsible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.codekillers.zelo.DTO.Mapper.ResponsibleMapper.toResponse;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private ResponsibleService responsibleService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {

            Optional<Responsible> responsibleOptional = responsibleService.getResponsibleByEmail(username);
            if (responsibleOptional.isPresent()) {
               return responsibleOptional.get();
            }

            //login idoso


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserResponse authenticateUser(String username) {
        try {

            Optional<Responsible> responsibleOptional = responsibleService.getResponsibleByEmail(username);
            if (responsibleOptional.isPresent()) {
                return toResponse(responsibleOptional.get());
            }

            //login idoso


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
