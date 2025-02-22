package com.example.drugmed.controller;

import com.example.drugmed.dto.patient.PatientCreateRequest;
import com.example.drugmed.entity.Patient;
import com.example.drugmed.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(PatientController.class)
//@ExtendWith(SpringExtension.class)
//class PatientControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private PatientRepository patientRepository;
//
//    @Test
//    void loginFailedNotFound() throws Exception {
//        PatientCreateRequest request = PatientCreateRequest.builder()
//                .fullName("John Doe")
//                .dateBirth(LocalDate.now())
//                .gender(Patient.Gender.valueOf("Male"))
//                .build();
//
//        Patient patient = Patient.builder()
//                .fullName(request.getFullName())
//                .dateBirth(request.getDateBirth())
//                .gender(request.getGender())
//                .build();
//
//        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
//
//        mockMvc.perform(
//                post("/api/v1/patient")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .contentType(objectMapper.writeValueAsString(request))
//        ).andExpect(status().isUnauthorized());
//    }
//}