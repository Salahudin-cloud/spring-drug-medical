package com.example.drugmed.dto.patient;

import com.example.drugmed.entity.Patient;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientUpdateRequest {

    private String fullName;
    private LocalDate dateBirth;
    private Patient.Gender gender;
}
