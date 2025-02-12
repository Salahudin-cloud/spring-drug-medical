package com.example.drugmed.dto.drug_detail;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugDetailResponse {

    private Long id;
    private String composition;
    private String dosage;
    private String desc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
