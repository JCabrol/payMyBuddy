package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.NotValidException;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonInscriptionDTO;
import com.openclassrooms.payMyBuddy.service.PersonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api
public class AdminController {
    @Autowired
    PersonService personService;

    @Transactional
    @GetMapping("/admin")
    public ModelAndView admin() {
        String viewName = "admin";
        Map<String, Object> model = new HashMap<>();
        try {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
        } catch (Exception ignored) {
        }
        model.put("activePage", "Admin");
        return new ModelAndView(viewName, model);
    }
}
