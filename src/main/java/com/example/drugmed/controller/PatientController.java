package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.patient.PatientCreateRequest;
import com.example.drugmed.dto.patient.PatientResponse;
import com.example.drugmed.dto.patient.PatientUpdateRequest;
import com.example.drugmed.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PatientController {

    private final PatientService patientService;

    @PostMapping(
            path = "/patient",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> createPatient(@Valid @RequestBody PatientCreateRequest request){
        return patientService.createPatient(request);
    }


    @PatchMapping(
            path = "/patient",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> updatePatient(
            @RequestParam Long patient_id,
            @Valid @RequestBody PatientUpdateRequest request){
        return patientService.updatePatient(patient_id, request);
    }

    @DeleteMapping(
            path = "/patient",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> updatePatient(
            @RequestParam Long patient_id){
        return patientService.deletePatient(patient_id);
    }

    @GetMapping(
            path = "/patient",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PatientResponse>> getAllResponse(){
        return patientService.getAllPatient();
    }

    @GetMapping(
            path = "/patient/get",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<PatientResponse> getPatientById(@RequestParam  Long patient_id){
        return patientService.getPatientById(patient_id);
    }

}
