package com.example.drugmed.dto.patient_referral_letter;

import com.example.drugmed.dto.examination.ExaminationResponse;
import com.example.drugmed.dto.examination_result.ExaminationResultResponse;
import com.example.drugmed.entity.PatientReferralLetter;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PatientReferralLetterResponse {
    private Long id;
    private Long patientId;
    private LocalDateTime referralDate;
    private String doctorName;
    private String verifierName;
    private PatientReferralLetter.ReferralStatus isVerified;
    private List<ExaminationResponse> examinations;
}
