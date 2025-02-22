package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.prescription_claim.PrescriptionClaimRequest;
import com.example.drugmed.dto.prescription_claim.PrescriptionClaimResponse;
import com.example.drugmed.service.PrescriptionClaimService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PrescriptionClaimController {

    private final PrescriptionClaimService prescriptionClaimService;

    @PostMapping(
            path = "/prescription/claim",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> claim(@RequestParam Long prescription_id){
        return prescriptionClaimService.claimPrescription(prescription_id);
    }

    @GetMapping(
            path = "/prescription/claim",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PrescriptionClaimResponse>> getAllClaims(){
        return prescriptionClaimService.getAllClaims();
    }

    @GetMapping(
            path = "/prescription/claim/get",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PrescriptionClaimResponse>> getClaimsByPrescriptionId(@RequestParam Long prescription_id){
        return prescriptionClaimService.getPrescriptionById(prescription_id);
    }

}
