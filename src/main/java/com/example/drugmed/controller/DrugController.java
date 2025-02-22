package com.example.drugmed.controller;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug.DrugCreateRequest;
import com.example.drugmed.dto.drug.DrugResponse;
import com.example.drugmed.dto.drug.DrugUpdateRequest;
import com.example.drugmed.dto.patient.PatientResponse;
import com.example.drugmed.service.DrugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class DrugController {

    private final DrugService drugService;

    @PostMapping(
            path = "/drug",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> create(@Valid @RequestBody  DrugCreateRequest request) {
        return drugService.createDrug(request);
    }


    @PatchMapping(
            path = "/drug",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> update(Long drug_id, @Valid @RequestBody DrugUpdateRequest request) {
        return drugService.updateDrug(drug_id, request);
    }

    @DeleteMapping(
            path = "/drug",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(Long drug_id) {
        return drugService.deleteDrug(drug_id);
    }

    @GetMapping(
            path = "/drug",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<DrugResponse>> getAll() {
        return drugService.getAllDrugDetail();
    }


    @GetMapping(
            path = "/drug/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DrugResponse> getDrugResponseById(@PathVariable Long id) {


        return drugService.getDrugById(id);
    }


}
