package com.example.drugmed.dto.drug;



import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.entity.Drug;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugResponse {

    private Long id;
    private String drugName;
    private Drug.DrugCategory category;
    private int price;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DrugDetailResponse drugDetail;
}
