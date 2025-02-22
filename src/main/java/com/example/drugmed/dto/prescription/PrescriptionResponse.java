package com.example.drugmed.dto.prescription;


import com.example.drugmed.dto.drug.DrugPatientResponse;
import com.example.drugmed.dto.drug.DrugResponse;
import com.example.drugmed.dto.prescription_claim.PrescriptionClaimResponse;
import com.example.drugmed.entity.Drug;
import com.example.drugmed.entity.PrescriptionClaim;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionResponse {

    private Long id;
    private String doctorName;
    private LocalDateTime prescriptionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DrugPatientResponse> drugs;
    private List<PrescriptionClaimResponse> claimHistory;

}
