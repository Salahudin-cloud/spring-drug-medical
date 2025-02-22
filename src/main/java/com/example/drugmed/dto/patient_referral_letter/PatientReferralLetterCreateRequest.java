package com.example.drugmed.dto.patient_referral_letter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientReferralLetterCreateRequest {

    @NotNull(message = "Patient id are required")
    private Long patientId;
    @NotEmpty(message = "Doctor name are required")
    private String doctorName;
    @NotNull(message = "Referral date are required")
    private LocalDateTime referralDate;
    @NotNull(message = "Examination are required")
    private List<Long> examinationsIds;

}
