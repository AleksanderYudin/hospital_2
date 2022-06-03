package com.example.hospital_2.repository;

import com.example.hospital_2.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUsername(String username);

    Patient findByActivationCode(String code);
}
