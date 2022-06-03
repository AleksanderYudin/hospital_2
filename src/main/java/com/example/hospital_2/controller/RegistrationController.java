package com.example.hospital_2.controller;

import com.example.hospital_2.model.Patient;
import com.example.hospital_2.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/hospital")
public class RegistrationController {

    private final PatientService patientService;
    public RegistrationController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("patient", new Patient());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("patient") @Valid Patient patient, BindingResult bindingResult,
                          Model model) {
        if(bindingResult.hasErrors()) return "registration";
        if(!patientService.addUser(patient)) {
            model.addAttribute("patient", new Patient());
            model.addAttribute("message",
                    "Пользователь с таким логином уже существует!");
            return "registration";
        }
        else {
            model.addAttribute("patient", new Patient());
            model.addAttribute("message",
                    "Пользователь успешно добавлен!");
            return "redirect:/hospital";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = patientService.activatePatient(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }

}
