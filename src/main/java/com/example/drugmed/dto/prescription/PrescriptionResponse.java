package com.example.drugmed.dto.prescription;


import com.example.drugmed.dto.drug.DrugPatientResponse;
import com.example.drugmed.dto.drug.DrugResponse;
import com.example.drugmed.entity.Drug;
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
    private Long patient_id;
    private String doctorName;
    private LocalDateTime prescriptionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DrugPatientResponse> drugs;

}
