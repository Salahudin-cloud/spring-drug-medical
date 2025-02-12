package com.example.drugmed.dto.drug_detail;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrugDetailUpdateRequest {

    private Long id;
    private String composition;
    private String dosage;
    private String desc;

}
