package com.example.drugmed.dto.examination_result;

import com.example.drugmed.dto.examination_result_detail.ExaminationResultDetailCreateRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationResultCreateRequest {

    @NotNull(message = "Referral Letter id are required")
    private Long referralLetterId;

    @NotNull(message = "Examination id are required")
    private Long examinationId;

    @NotNull(message = "Verifier name are required")
    private String verifierName;

    @NotNull(message = "Result date are required")
    private LocalDateTime resultDate;

    @NotEmpty(message = "Examiner name are required")
    private String examinerName;

    @NotEmpty(message = "Result detail are required")
    private List<ExaminationResultDetailCreateRequest> listResultDetail;
}
