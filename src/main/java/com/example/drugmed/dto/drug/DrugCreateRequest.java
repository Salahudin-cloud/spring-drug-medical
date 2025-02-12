package com.example.drugmed.dto.drug;


import com.example.drugmed.entity.Drug;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugCreateRequest {

    @NotBlank(message = "Drug name is required")
    private String drugName;

    @NotNull(message = "Drug category is required")
    private Drug.DrugCategory category;

    @Min(value = 0, message = "Price must be at least 0")
    private Integer price;

    @Min(value = 0, message = "Stock must be at least 0")
    private Integer stock;

    @NotBlank(message = "Composition is required")
    private String composition;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Drug description is required")
    private String description;
}
