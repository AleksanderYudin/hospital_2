package com.example.hospital_2.controller;

import com.example.hospital_2.Hospital2Application;
import com.example.hospital_2.model.DoctorDto;
import com.example.hospital_2.model.Patient;
import com.example.hospital_2.service.KafkaService;
import com.example.hospital_2.service.PatientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Controller
@RequestMapping("hospital")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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

    @GetMapping("appointments/doctors")
    public String getDoctorsList(Model model) {
        model.addAttribute("doctorsList", kafkaService.getDoctorsList());
        return "doctors_list";
    }

    @GetMapping("appointments/doctors/{id}")
    public String getAppointments(@PathVariable Long id, Model model){
        model.addAttribute("doctorsList", kafkaService.getAppointments(id));
        return "appointments";
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
