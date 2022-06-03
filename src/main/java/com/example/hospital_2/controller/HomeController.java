package com.example.hospital_2.controller;

import com.example.hospital_2.model.Patient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/hospital")
    public String homePage(@AuthenticationPrincipal Patient patient, Model model) {
        model.addAttribute("patient", patient);
        return "home_page";
    }
}
