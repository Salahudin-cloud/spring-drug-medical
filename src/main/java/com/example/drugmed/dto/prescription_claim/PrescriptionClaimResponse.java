package com.example.drugmed.dto.prescription_claim;


import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionClaimResponse {

    private Long id;
    private Long prescriptionId;
    private LocalDateTime claimAt;

}
