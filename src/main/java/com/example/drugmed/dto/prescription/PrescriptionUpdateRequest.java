package com.example.drugmed.dto.prescription;


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

        private Long patientId;
        private List<PrescriptionDrugUpdateRequest> drugUpdateRequest;
        private String doctorName;
        private LocalDateTime prescriptionDate;


    }
