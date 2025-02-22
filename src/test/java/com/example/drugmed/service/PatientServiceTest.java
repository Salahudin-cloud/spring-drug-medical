package com.example.drugmed.service;

import com.example.drugmed.controller.PatientController;
import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.patient.PatientCreateRequest;
import com.example.drugmed.entity.Patient;
import com.example.drugmed.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

   @Test
    void createPatient() {
       PatientCreateRequest patient = PatientCreateRequest.builder()
               .fullName("John Doe")
               .dateBirth(LocalDate.now())
               .gender(Patient.Gender.valueOf("MALE"))
               .build();

        Patient savedPatient = Patient.builder()
                .fullName(patient.getFullName())
                .dateBirth(patient.getDateBirth())
                .gender(patient.getGender())
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        WebResponse<String> result = patientService.createPatient(patient);

       assertNotNull(result);
       assertEquals("Berhasil Menambahkan Pasien", result.getMessage());
       assertEquals(200, result.getStatus());

       verify(patientRepository, times(1)).save(any(Patient.class));
   }


    @Test
    void createdPatientFailed_WhenGenderIsNull() {

        PatientCreateRequest patient = PatientCreateRequest.builder()
                .fullName("John Doe")
                .dateBirth(LocalDate.now())
                .gender(null)
                .build();

        // Act & Assert: Expecting an exception to be thrown
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> patientService.createPatient(patient));

        assertEquals("400 BAD_REQUEST \"Gender are required\"", exception.getMessage());

        // Verify that repository save was never called
        verify(patientRepository, never()).save(any(Patient.class));
    }



}