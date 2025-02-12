package com.example.drugmed.controller;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.prescription.PrescriptionCreateRequest;
import com.example.drugmed.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping(
            path = "/prescription",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> create(@Valid @RequestBody PrescriptionCreateRequest request) {
        return prescriptionService.createPrescription(request);
    }

    @DeleteMapping(
            path = "/prescription",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> delete(Long precription_id) {
        return prescriptionService.deletePrescriptionById(precription_id);
    }

}
