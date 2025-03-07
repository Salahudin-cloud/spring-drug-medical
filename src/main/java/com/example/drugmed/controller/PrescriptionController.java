package com.example.drugmed.controller;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.prescription.PrescriptionCreateRequest;
import com.example.drugmed.dto.prescription.PrescriptionResponse;
import com.example.drugmed.dto.prescription.PrescriptionUpdateRequest;
import com.example.drugmed.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public WebResponse<Void> delete(@RequestParam  Long precription_id) {
        return prescriptionService.deletePrescriptionById(precription_id);
    }


    @PatchMapping(
            path = "/prescription",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> updatePrescriptionById(
            @RequestParam  Long precription_id,
            @RequestParam(required = false) Long pasien_id,
            @RequestBody  PrescriptionUpdateRequest request
    ) {
        return prescriptionService.UpdatePrescription(precription_id, pasien_id, request);
    }

    @GetMapping(
            path = "/prescription",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PrescriptionResponse>> getAllPrescription() {
        return prescriptionService.getAllPrescription();
    }
}
