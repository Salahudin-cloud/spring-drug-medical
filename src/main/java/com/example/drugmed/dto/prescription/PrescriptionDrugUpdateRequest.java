package com.example.drugmed.dto.prescription;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PrescriptionDrugUpdateRequest {

    private Long drugId;
    private Long oldDrugId;
    private ActionType action;


    public enum ActionType{
        ADD, REPLACE_ALL,DELETE,REPLACE
    }

}
