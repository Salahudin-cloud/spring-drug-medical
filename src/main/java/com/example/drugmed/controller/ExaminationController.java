package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.examination.ExaminationCreateRequest;
import com.example.drugmed.dto.examination.ExaminationResponse;
import com.example.drugmed.service.ExaminationService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ExaminationController {

    private final ExaminationService examinationService;

    @PostMapping(
            path = "/examination/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Void> createExamination(@Valid @RequestBody ExaminationCreateRequest request) {
        return examinationService.createExamination(request);
    }

    @GetMapping(
            path = "/examination/get-all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ExaminationResponse>> getAllExamination() {
        return examinationService.allExamination();
    }

}
