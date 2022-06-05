package com.example.hospital_2.controller;

import com.example.hospital_2.model.Patient;
import com.example.hospital_2.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("appointments")
    public String getAppointments(Model model){
        Map<String, List<String>> map = new HashMap<>();

        List<String> list1 = new ArrayList<>();
        list1.add("10:00");
        list1.add("12:00");
        list1.add("14:00");
        List<String> list2 = new ArrayList<>();
        list2.add("15:00");
        list2.add("16:00");
        map.put("Понедельник", list1);
        map.put("Вторник", list2);
        model.addAttribute("days", map);
        model.addAttribute("doctor", "Айболит");

        return "appointment";
    }

    @GetMapping("appointments/new")
    public String createAppointment(@RequestParam String doctor,
                                    @RequestParam String day,
                                    @RequestParam String time,
                                    Model model){
        model.addAttribute("doctor", doctor);
        model.addAttribute("day", day);
        model.addAttribute("time", time);

        return "appointment_ok";
    }

}
