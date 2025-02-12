package com.example.drugmed.controller;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailUpdateRequest;
import com.example.drugmed.service.DrugDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/drug")
public class DrugDetailController {

    private final DrugDetailService drugDetailService;


    @GetMapping(
            path = "/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<DrugDetailResponse>> getAllDetailDrug() {
        return drugDetailService.getAllDrugDetail();
    }

    @GetMapping(
            path = "/detail/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<DrugDetailResponse> getDrugDetailById(@PathVariable Long id){
        return drugDetailService.getDrugDetailById(id);
    }

    @PatchMapping(
            path = "/detail",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> updateDataDrugs(@RequestParam Long drug_detail_id, @RequestBody DrugDetailUpdateRequest request) {
        return drugDetailService.updateDrugDetail(drug_detail_id, request);
    }

}
