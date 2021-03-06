package com.example.cms.controllers;

import com.example.cms.models.User;
import com.example.cms.services.SecurityService;
import com.example.cms.services.UserService;
import com.example.cms.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/admin/registration")
    public String registration(Model model){
        model.addAttribute("userForm", new User());
        return "admin/registration";
    }

    @PostMapping("/admin/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult){
        userValidator.validate(userForm, bindingResult);

        if(bindingResult.hasErrors()){
            return "admin/registration";
        }
        userService.save(userForm);

        if(securityService.autoLogin(userForm.getUsername(),userForm.getPasswordConfirm())){
            return "redirect:/admin";
        }
        else{
            return "redirect:/admin/login";
        }

    }

    @GetMapping("/admin/login")
    public String adminLoginPage(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        model.addAttribute("userLoginForm", new User());

        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String login(@ModelAttribute("userLoginForm") User userLoginForm, BindingResult bindingResult){

        String username = userService.findByEmail(userLoginForm.getEmail()).getUsername();
        userLoginForm.setUsername(username);
        
        if(securityService.autoLogin(userLoginForm.getUsername(),userLoginForm.getPasswordConfirm())){
            return "redirect:/admin/";
        }
        else{
            return "redirect:/admin/login";
        }

    }



}
