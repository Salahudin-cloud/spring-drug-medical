package com.example.drugmed.service;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.examination_result.ExaminationResultCreateRequest;
import com.example.drugmed.dto.examination_result.ExaminationResultResponse;
import com.example.drugmed.dto.examination_result_detail.ExaminationResultDetailCreateRequest;
import com.example.drugmed.dto.examination_result_detail.ExaminationResultDetailResponse;
import com.example.drugmed.entity.*;
import com.example.drugmed.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExaminationResultService {

    private final ExaminationResultRepository examinationResultRepository;
    private final PatientReferralLetterRepository patientReferralLetterRepository;
    private final ListExaminationRepository listExaminationRepository;
    private final ExaminationResultDetailRepository examinationResultDetailRepository;

    @Transactional
    public WebResponse<Void> createExaminationResult(ExaminationResultCreateRequest request) {

        PatientReferralLetter oldLetter = patientReferralLetterRepository.findById(request.getReferralLetterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Surat keterangan dengan id " + request.getReferralLetterId() + " tidak ditemukan"));

       if (!listExaminationRepository.existsByExaminationId(request.getExaminationId())) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Surat pengantar dengan id " + request.getExaminationId() + " tidak ditemukan");
       }

        ExaminationResult newExaminationResult = ExaminationResult.builder()
                .examinerName(request.getExaminerName())
                .resultDate(request.getResultDate())
                .build();


        oldLetter.setVerifierName(request.getVerifierName());
        oldLetter.setIsVerified(PatientReferralLetter.ReferralStatus.VERIFIED);
        patientReferralLetterRepository.save(oldLetter);

        ListExamination updateListExamination = listExaminationRepository.findByExaminationIdAndReferralId(request.getExaminationId(), request.getReferralLetterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidka ditmukan"));

        updateListExamination.setExaminationResult(newExaminationResult);
        listExaminationRepository.save(updateListExamination);

        examinationResultRepository.save(newExaminationResult);

        for (ExaminationResultDetailCreateRequest detailRequest : request.getListResultDetail()) {
            ExaminationResultDetail detailEntity = ExaminationResultDetail.builder()
                    .examinationResult(newExaminationResult)
                    .detailResult(detailRequest.getResultDetail())
                    .build();
            examinationResultDetailRepository.save(detailEntity);
        }

        return WebResponse.<Void>builder()
                .message("Data hasil pemeriksaan berhasil dibuat")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<List<ExaminationResultResponse>> getAllExaminationResult() {
        List<ExaminationResult> getAll = examinationResultRepository.findAll();
        List<ExaminationResultResponse> responses = getAll.stream().map(
                x -> ExaminationResultResponse.builder()
                        .id(x.getId())
                        .examinerName(x.getExaminerName())
                        .resultDate(x.getResultDate())
                        .resultDetail(x.getDetails().stream().map(z ->
                                        ExaminationResultDetailResponse.builder()
                                                .id(z.getId())
                                                .examinationResultId(z.getExaminationResult().getId())
                                                .detailResult(z.getDetailResult())
                                                .build()
                                ).toList())
                        .build()
        ).toList();

        return WebResponse.<List<ExaminationResultResponse>>builder()
                .message("Data semua hasil pemeriksaan didapatkan")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();

    }


    public WebResponse<Void> deleteResult(Long id) {
        ExaminationResult result = examinationResultRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hasil pemeriksaan dengan id " + id + " tidak ditemukan"));

        listExaminationRepository.updateExaminationResultToNull(id);

        examinationResultRepository.delete(result);

        return WebResponse.<Void>builder()
                .message("Berhasil menghapus hasil pemeriksaan")
                .status(HttpStatus.OK.value())
                .build();
    }

}