package com.example.drugmed.dto.examination_result_detail;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExaminationResultDetailResponse {
    private Long id;
    private Long examinationResultId;
    private String detailResult;
}
