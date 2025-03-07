package com.example.drugmed.dto.patient_referral_letter;

import com.example.drugmed.dto.prescription.PrescriptionDrugUpdateRequest;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PatientReferralLetterExaminationUpdateRequest {
    private Long referralId;
    private Long oldExamination;
    private Long newExamination;
    private List<Long> examinationIds;
    private ActionType action;
    public enum ActionType{
        ADD, REPLACE_ALL,DELETE,REPLACE
    }
}
