package com.example.drugmed.dto.examination_result_detail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationResultDetailCreateRequest {

    private Long resultExaminationId;
    private String resultDetail;

}
