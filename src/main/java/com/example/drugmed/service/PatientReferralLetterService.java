package com.example.drugmed.service;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.examination.ExaminationResponse;
import com.example.drugmed.dto.examination_result.ExaminationResultResponse;
import com.example.drugmed.dto.examination_result_detail.ExaminationResultDetailResponse;
import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterCreateRequest;
import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterResponse;
import com.example.drugmed.dto.projection.PatientReferralLetterProjection;
import com.example.drugmed.entity.*;
import com.example.drugmed.repository.ExaminationRepository;
import com.example.drugmed.repository.ListExaminationRepository;
import com.example.drugmed.repository.PatientReferralLetterRepository;
import com.example.drugmed.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientReferralLetterService {

    private final PatientReferralLetterRepository patientReferralLetterRepository;
    private final PatientRepository patientRepository;
    private final ListExaminationRepository listExaminationRepository;
    private final ExaminationRepository examinationRepository;

    @Transactional
    public WebResponse<Void> createReferralLetter(PatientReferralLetterCreateRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pasien dengan id " + request.getPatientId() + " tidak ditemukan"));

        List<Examination> examinations = examinationRepository.findAllById(request.getExaminationsIds());

        if (examinations.size() != request.getExaminationsIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beberapa pemeriksaan tidak ditemukan");
        }

        PatientReferralLetter newReferralLetter = PatientReferralLetter.builder()
                .patient(patient)
                .doctorName(request.getDoctorName())
                .referralDate(request.getReferralDate())
                .isVerified(PatientReferralLetter.ReferralStatus.NOT_VERIFIED)
                .build();

        List<ListExamination> listExaminations = examinations.stream()
                .map(exam -> ListExamination.builder()
                        .referralLetter(newReferralLetter)
                        .examination(exam)
                        .build())
                .toList();

        patientReferralLetterRepository.save(newReferralLetter);
        listExaminationRepository.saveAll(listExaminations);

        return WebResponse.<Void>builder()
               .message("Surat pengantar berhasi dibuat")
                .status(HttpStatus.OK.value())
               .build();
    }

    public WebResponse<List<PatientReferralLetterResponse>> getAllReferral() {
        List<PatientReferralLetterProjection> patientLetter = patientReferralLetterRepository.getAllReferral();


        Map<Long, List<PatientReferralLetterProjection>> patientLetterMap =
                patientLetter.stream().collect(Collectors.groupingBy(PatientReferralLetterProjection::getReferralId));

        List<PatientReferralLetterResponse> responses = patientLetterMap.entrySet().stream().map(entry -> {
            Long patientReferralId = entry.getKey();
            List<PatientReferralLetterProjection> records = entry.getValue();
            PatientReferralLetterProjection firstRecord = records.getFirst();


            Map<Long, List<PatientReferralLetterProjection>> examinationMap =
                    records.stream().filter(x -> x.getExaminationId() != null)
                            .collect(Collectors.groupingBy(PatientReferralLetterProjection::getExaminationId));

            List<ExaminationResponse> examResponse = examinationMap.entrySet().stream().map(exam -> {
                Long examinationId = exam.getKey();
                List<PatientReferralLetterProjection> examRecords = exam.getValue();
                PatientReferralLetterProjection examFirstRecord = examRecords.getFirst();


                Map<Long, List<PatientReferralLetterProjection>> examResultMap =
                        examRecords.stream().filter(x -> x.getExaminationResultId() != null)
                                .collect(Collectors.groupingBy(PatientReferralLetterProjection::getExaminationResultId));

                List<ExaminationResultResponse> listExaminationResult = examResultMap.entrySet().stream().map(examResult -> {
                    Long examinationResultId = examResult.getKey();
                    List<PatientReferralLetterProjection> examResultRecords = examResult.getValue();
                    PatientReferralLetterProjection examResultFirstRecord = examResultRecords.getFirst();


                    Map<Long, List<PatientReferralLetterProjection>> examDetailResultMap =
                            examResultRecords.stream().filter(x -> x.getResultDetailId() != null)
                                    .collect(Collectors.groupingBy(PatientReferralLetterProjection::getResultDetailId));

                    List<ExaminationResultDetailResponse> listExaminationResultDetail = examDetailResultMap.entrySet().stream().map(examDetailResult -> {
                        Long examinationResultDetailId = examDetailResult.getKey();
                        List<PatientReferralLetterProjection> examDetailResultRecords = examDetailResult.getValue();
                        PatientReferralLetterProjection examResultDetailFirstRecord = examDetailResultRecords.getFirst();

                        return ExaminationResultDetailResponse.builder()
                                .id(examinationResultDetailId)
                                .examinationResultId(examResultDetailFirstRecord.getExaminationResultId())
                                .detailResult(examResultDetailFirstRecord.getDetailResult())
                                .build();
                    }).toList();

                    return ExaminationResultResponse.builder()
                            .id(examinationResultId)
                            .examinerName(examResultFirstRecord.getExaminerName())
                            .resultDate(examResultFirstRecord.getResultDate())
                            .resultDetail(listExaminationResultDetail)
                            .build();
                }).toList();

                return ExaminationResponse.builder()
                        .id(examinationId)
                        .examination(examFirstRecord.getExaminationName())
                        .examinationDetail(examFirstRecord.getExaminationDesc())
                        .price(examFirstRecord.getPrice())
                        .createdAt(examFirstRecord.getExaminationCreatedAt())
                        .updatedAt(examFirstRecord.getExaminationUpdateAt())
                        .examinationResults(listExaminationResult)
                        .build();
            }).toList();

            return PatientReferralLetterResponse.builder()
                    .id(patientReferralId)
                    .patientId(firstRecord.getPatientId())
                    .referralDate(firstRecord.getReferralDate())
                    .doctorName(firstRecord.getDoctorName())
                    .verifierName(firstRecord.getVerifierName())
                    .isVerified(PatientReferralLetter.ReferralStatus.valueOf(firstRecord.getIsVerified()))
                    .examinations(examResponse)
                    .build();
        }).toList();

        return WebResponse.<List<PatientReferralLetterResponse>>builder()
                .message("Data surat pengantar berhasil didapatkan")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
    }


    public WebResponse<PatientReferralLetterResponse> getReferralId(Long id) {
        List<PatientReferralLetterProjection> referrraldataList = patientReferralLetterRepository.getAllReferralPatientId(id);

        if (referrraldataList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Referral dengan id pasien " + id + " tidak ditemukan");
        }

        PatientReferralLetterProjection referralData = referrraldataList.getFirst();

        Map<Long, ExaminationResponse> examinationMap = new HashMap<>();

        for (PatientReferralLetterProjection data : referrraldataList) {
            examinationMap.computeIfAbsent(data.getExaminationId(), examId ->
                    ExaminationResponse.builder()
                            .id(data.getExaminationId())
                            .examination(data.getExaminationName())
                            .price(data.getPrice())
                            .examinationDetail(data.getExaminationDesc())
                            .createdAt(data.getExaminationCreatedAt())
                            .updatedAt(data.getExaminationUpdateAt())
                            .examinationResults(new ArrayList<>())
                            .build());

            if (data.getExaminationResultId() != null) {
                ExaminationResultResponse resultResponse = ExaminationResultResponse.builder()
                        .id(data.getExaminationResultId())
                        .examinerName(data.getExaminerName())
                        .resultDate(data.getResultDate())
                        .resultDetail(new ArrayList<>())
                        .build();

                examinationMap.get(data.getExaminationId()).getExaminationResults().add(resultResponse);
            }

            if (data.getResultDetailId() != null) {
                ExaminationResultDetailResponse detailResponse = ExaminationResultDetailResponse.builder()
                        .id(data.getResultDetailId())
                        .examinationResultId(data.getExaminationResultId())
                        .detailResult(data.getDetailResult())
                        .build();


                ExaminationResponse examinationResponse = examinationMap.get(data.getExaminationId());

                examinationResponse.getExaminationResults()
                        .stream()
                        .filter(result -> result.getId().equals(data.getExaminationResultId()))
                        .findFirst()
                        .ifPresent(result -> result.getResultDetail().add(detailResponse));
            }
        }

        PatientReferralLetterResponse response = PatientReferralLetterResponse.builder()
                .id(referralData.getReferralId())
                .patientId(referralData.getPatientId())
                .referralDate(referralData.getReferralDate())
                .doctorName(referralData.getDoctorName())
                .verifierName(referralData.getVerifierName())
                .isVerified(PatientReferralLetter.ReferralStatus.valueOf(referralData.getIsVerified()))
                .examinations(new ArrayList<>(examinationMap.values()))
                .build();


        return WebResponse.<PatientReferralLetterResponse>builder()
                .message("Data surat pengantar didapatkan")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }
}
