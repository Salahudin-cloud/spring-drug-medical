package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.examination.ExaminationCreateRequest;
import com.example.drugmed.dto.examination.ExaminationResponse;
import com.example.drugmed.dto.examination.ExaminationUpdateRequest;
import com.example.drugmed.entity.Examination;
import com.example.drugmed.repository.ExaminationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

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
                        .examination(x.getExamination())
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

    @Transactional
    public WebResponse<Void> examinationUpdate(Long id, ExaminationUpdateRequest request) {
        if (request == null || (new ObjectMapper().convertValue(request, Map.class)).isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body tidak boleh kosong");
        }

        Examination examOldData = examinationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pemeriksaan dengan id " + id + " tidak ditemukan"));

        if (request.getExaminationName() != null) {
            examOldData.setExamination(request.getExaminationName());
        }
        if (request.getExaminationDesc() != null) {
            examOldData.setExaminationDesc(request.getExaminationDesc());
        }
        if (request.getPrice() != null) {
            System.out.println("set harga pemeriksaan");
            examOldData.setPrice(request.getPrice());
        }

        examinationRepository.save(examOldData);

        return WebResponse.<Void>builder()
                .message("Berhasil mengupdate data pemeriksaan")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Transactional
    public WebResponse<Void> deleteExamination(Long id) {
        Examination examination = examinationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pemeriksaan dengan id " + id + "tidak ditemukan"));

        examinationRepository.delete(examination);

        return WebResponse.<Void>builder()
                .message("Data pemeriksaan berhasil dihapus")
                .status(HttpStatus.OK.value())
                .build();
    }
}
