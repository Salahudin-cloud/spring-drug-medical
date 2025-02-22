package com.example.drugmed.dto.examination;

import com.example.drugmed.dto.examination_result.ExaminationResultResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationResponse {
    private Long id;
    private String examination;
    private Integer price;
    private String examinationDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ExaminationResultResponse> examinationResults;
}
