package com.example.hospital_2.service;

import com.example.hospital_2.model.Patient;
import com.example.hospital_2.model.Role;
import com.example.hospital_2.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class PatientService implements UserDetailsService {

    private final PatientRepository patientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepository.findByUsername(username);
    }

    public boolean addUser(Patient patient){
        Patient patientFromDB = patientRepository.findByUsername(patient.getUsername());
        if(patientFromDB != null) return false;

        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRole(Role.PATIENT);
        patient.setActivationCode(UUID.randomUUID().toString());
        patientRepository.save(patient);

        if(!patient.getEmail().isEmpty()) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Hospital. Please, visit next link: http://localhost:8075/hospital/activate/%s",
                    patient.getUsername(),
                    patient.getActivationCode()
            );
            mailSender.send(patient.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activatePatient(String code) {
        Patient patient = patientRepository.findByActivationCode(code);
        if(patient == null)
            return false;
        patient.setActivationCode(null);
        patientRepository.save(patient);
        return true;
    }
}
