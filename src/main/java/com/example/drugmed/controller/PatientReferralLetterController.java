package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterCreateRequest;
import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterResponse;
import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterUpdateRequest;
import com.example.drugmed.service.PatientReferralLetterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PatientReferralLetterController {

    private final PatientReferralLetterService patientReferralLetterService;

    @PostMapping(
            path = "/referral-letter/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> createReferralLetter(@Valid @RequestBody  PatientReferralLetterCreateRequest request) {
        return patientReferralLetterService.createReferralLetter(request);
    }

    @GetMapping(
            path = "/referral-letter/get-all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PatientReferralLetterResponse>> getAllPatientReferralLetter() {
        return patientReferralLetterService.getAllReferral();
    }

    @GetMapping(
            path = "/referral-letter/get",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<PatientReferralLetterResponse> getReferralByPatientId(@RequestParam Long patient_id) {
        return patientReferralLetterService.getReferralId(patient_id);
    }

    @DeleteMapping(
            path = "/referral-letter",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> deleteExaminationReferral(@RequestParam Long examination_id, @RequestParam Long referral_id) {
        return patientReferralLetterService.deleteExamination(examination_id, referral_id);
    }

    @PatchMapping(
            path = "/referral-letter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> updateReferralId(@RequestParam Long referral_id, @RequestBody PatientReferralLetterUpdateRequest request) {
        return patientReferralLetterService.updateReferralIdLong(referral_id, request);
    }

}
