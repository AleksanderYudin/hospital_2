package com.example.hospital_2.controller;

import com.example.hospital_2.Hospital2Application;
import com.example.hospital_2.model.DoctorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
public class KafkaListeners {

    ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics="hospital_to_hospital2_doctorsList")
    public void orderListener(ConsumerRecord<String, String> record){
//        System.out.println(record.partition());
//        System.out.println(record.key());
//        System.out.println(record.value());
        Hospital2Application.futureMap.get(record.key()).complete(record.value());
    }
}
