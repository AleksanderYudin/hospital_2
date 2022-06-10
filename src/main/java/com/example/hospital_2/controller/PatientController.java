package com.example.hospital_2.controller;

import com.example.hospital_2.Hospital2Application;
import com.example.hospital_2.model.DoctorDto;
import com.example.hospital_2.model.Patient;
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
    private KafkaTemplate<String, String> kafkaTemplate;

    ObjectMapper objectMapper = new ObjectMapper();


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

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String uuid = UUID.randomUUID().toString();
        Hospital2Application.futureMap.put(uuid, completableFuture);
        System.out.println("CurrentThread " + Thread.currentThread().toString() + ", time: " + OffsetDateTime.now());
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                .send("hospital2_to_hospital_doctorsList", uuid, "doctorsList");
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();

        System.out.println("Запрос с uuid = " + uuid + " отправлен...");
        String result = null;
        try {
            result = completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Объект CompletableFuture вернул результат...");
        Hospital2Application.futureMap.remove(uuid);
        List<DoctorDto> doctorsList = null;
        try {
            doctorsList = objectMapper.readValue(result, new TypeReference<List<DoctorDto>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("doctorsList", doctorsList);
        System.out.println("Запрос с uuid = " + uuid + " обработан...");
        return "doctors_list";
    }

    @GetMapping("appointments/doctors/{id}")
    public String getAppointments(@PathVariable Long id, Model model){
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

        model.addAttribute("id", id);
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
