package com.example.drugmed.dto.drug;


import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.entity.Drug;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugPatientResponse {

    private Long id;
    private String drugName;
    private Drug.DrugCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DrugDetailResponse drugDetail;

}
