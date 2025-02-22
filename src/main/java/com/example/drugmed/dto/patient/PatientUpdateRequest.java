package com.example.drugmed.dto.patient;

import com.example.drugmed.entity.Patient;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
