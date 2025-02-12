package com.example.drugmed.dto.patient;


import com.example.drugmed.dto.prescription.PrescriptionResponse;
import com.example.drugmed.entity.Patient;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {

    private Long id;
    private String fullName;
    private LocalDate dateBirth;
    private Patient.Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PrescriptionResponse> prescription;


}
