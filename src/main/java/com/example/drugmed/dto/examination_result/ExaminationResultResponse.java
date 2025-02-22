package com.example.drugmed.dto.examination_result;

import com.example.drugmed.dto.examination_result_detail.ExaminationResultDetailResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExaminationResultResponse {
    private Long id;
    private String examinerName;
    private LocalDateTime resultDate;
    private List<ExaminationResultDetailResponse> resultDetail;
}
