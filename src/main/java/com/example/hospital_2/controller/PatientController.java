package com.example.hospital_2.controller;

import com.example.hospital_2.model.Patient;
import com.example.hospital_2.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("hospital")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("profile")
    public String getProfile(@AuthenticationPrincipal Patient patient, Model model){
        model.addAttribute("firstName", patient.getFirstName());
        model.addAttribute("secondName", patient.getSecondName());
        model.addAttribute("insurance", patient.getInsurance());
        model.addAttribute("email", patient.getEmail());
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal Patient patient,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String insurance){
        patientService.updateProfile(patient, email, password, insurance);
        return "redirect:/hospital";
    }
}
