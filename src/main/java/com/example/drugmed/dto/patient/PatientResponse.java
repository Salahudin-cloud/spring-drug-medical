package com.example.drugmed.dto.patient;


import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterResponse;
import com.example.drugmed.dto.prescription.PrescriptionResponse;
import com.example.drugmed.entity.Patient;
import com.example.drugmed.entity.PatientReferralLetter;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {

    private Long id;
    private String fullName;
    private LocalDate dateBirth;
    private Patient.Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PatientReferralLetterResponse> referralLetters;
    private List<PrescriptionResponse> prescriptions;

}
