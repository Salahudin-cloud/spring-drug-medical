package com.example.drugmed.dto.prescription;


import com.example.drugmed.entity.Prescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionCreateRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotEmpty(message = "Drug IDs are required")
    private List<Long> drugIds;

    @NotBlank(message = "Doctor name is required")
    private String doctorName;


    @NotNull(message = "claim limit are required")
    private Prescription.ClaimLimit claim;

    @NotNull(message = "Prescription date is required")
    private LocalDateTime prescriptionDate;
}
