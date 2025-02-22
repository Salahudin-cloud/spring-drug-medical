package com.example.drugmed.dto.examination;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Builder
public class ExaminationCreateRequest {

    @NotEmpty(message = "Examination are required")
    private String examinationName;

    @NotEmpty(message = "Examination description are required")
    private String examinationDesc;

    @NotNull(message = "Price are required")
    private Integer price;


}
