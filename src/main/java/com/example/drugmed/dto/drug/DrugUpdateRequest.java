package com.example.drugmed.dto.drug;

import com.example.drugmed.entity.Drug;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugUpdateRequest {

    private String drugName;
    private Drug.DrugCategory category;
    private Integer price;
    private Integer stock;

}
