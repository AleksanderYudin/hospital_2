package com.example.hospital_2.service;


import com.example.hospital_2.Hospital2Application;
import com.example.hospital_2.model.DoctorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public List<DoctorDto> getDoctorsList() {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String uuid = UUID.randomUUID().toString();
        Hospital2Application.futureMap.put(uuid, completableFuture);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                .send("hospital2_to_hospital_doctorsList", uuid, "doctorsList");
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();

        String result = null;
        try {
            result = completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        Hospital2Application.futureMap.remove(uuid);

        List<DoctorDto> doctorsList = null;
        try {
            doctorsList = objectMapper.readValue(result, new TypeReference<List<DoctorDto>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return doctorsList;
    }

    public DoctorDto getAppointments(Long id) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String uuid = UUID.randomUUID().toString();
        Hospital2Application.futureMap.put(uuid, completableFuture);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate
                .send("hospital2_to_hospital_doctorsList", uuid, id.toString());
        future.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();

        String result = null;
        try {
            result = completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        Hospital2Application.futureMap.remove(uuid);

        DoctorDto doctorDto = null;
        try {
            doctorDto = objectMapper.readValue(result, DoctorDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return doctorDto;
    }
}
