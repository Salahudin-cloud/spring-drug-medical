package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.examination.ExaminationCreateRequest;
import com.example.drugmed.dto.examination.ExaminationResponse;
import com.example.drugmed.entity.Examination;
import com.example.drugmed.repository.ExaminationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;


    public WebResponse<Void> createExamination(ExaminationCreateRequest request) {
        Examination newExamination  = Examination.builder()
                .examination(request.getExaminationName())
                .examinationDesc(request.getExaminationDesc())
                .price(request.getPrice())
                .build();

        examinationRepository.save(newExamination);

        return WebResponse.<Void>builder()
                .message("Data pemeriksaan berhasi dibuat")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<List<ExaminationResponse>> allExamination() {
        List<Examination> examination = examinationRepository.findAll();

        List<ExaminationResponse> responses = examination.stream().map(
                x -> ExaminationResponse.builder()
                        .id(x.getId())
                        .examinationDetail(x.getExamination())
                        .price(x.getPrice())
                        .examinationDetail(x.getExaminationDesc())
                        .createdAt(x.getCreatedAt())
                        .updatedAt(x.getUpdatedAt())
                        .build()).toList();



        return WebResponse.<List<ExaminationResponse>>builder()
                .message("Berhasil mendapatkan data pemeriksaaan")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
    }

}
