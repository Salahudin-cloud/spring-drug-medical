package com.example.drugmed.dto.patient_referral_letter;

import com.example.drugmed.entity.PatientReferralLetter;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientReferralLetterUpdateRequest {

    private Long patientId;
    private String doctorName;
    private String verifierName;
    private LocalDateTime referralDate;
    private PatientReferralLetter.ReferralStatus isVerified;
    private List<PatientReferralLetterExaminationUpdateRequest> listExamination;
}
