package com.example.drugmed.dto.examination;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationUpdateRequest {

    private String examinationName;
    private String examinationDesc;
    private Integer price;

}
