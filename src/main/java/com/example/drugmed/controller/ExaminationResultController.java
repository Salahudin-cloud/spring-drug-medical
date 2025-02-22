package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.examination_result.ExaminationResultCreateRequest;
import com.example.drugmed.service.ExaminationResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ExaminationResultController {

    private final ExaminationResultService examinationResultService;

    @PostMapping(
            path = "/examination-result/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> createExaminationResult(@Valid @RequestBody ExaminationResultCreateRequest request) {
        return examinationResultService.createExaminationResult(request);
    }

}
