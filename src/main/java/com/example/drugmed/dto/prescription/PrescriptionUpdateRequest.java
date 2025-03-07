package com.example.drugmed.dto.prescription;


import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterExaminationUpdateRequest;
import com.example.drugmed.entity.Drug;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionUpdateRequest {

    private List<PrescriptionDrugUpdateRequest> drug;
    private String doctorName;
    private LocalDateTime prescriptionDate;


}
