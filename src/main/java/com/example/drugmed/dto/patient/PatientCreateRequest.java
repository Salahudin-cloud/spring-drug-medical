package com.example.drugmed.dto.patient;


import com.example.drugmed.entity.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientCreateRequest {

    @NotBlank(message = "Full name are required")
    private String fullName;

    @NotNull(message = "Date Birth are required")
    private LocalDate dateBirth;

    @NotNull(message = "Gender are required")
    private Patient.Gender gender;


}
