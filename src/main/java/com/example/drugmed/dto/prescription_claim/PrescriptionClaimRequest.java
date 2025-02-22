package com.example.drugmed.dto.prescription_claim;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionClaimRequest {

    @NotNull(message = "prescription id are required")
    private Long prescriptionId;

}
