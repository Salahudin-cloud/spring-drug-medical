package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug.DrugPatientResponse;
import com.example.drugmed.dto.drug.DrugResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.dto.examination.ExaminationResponse;
import com.example.drugmed.dto.examination_result.ExaminationResultResponse;
import com.example.drugmed.dto.examination_result_detail.ExaminationResultDetailResponse;
import com.example.drugmed.dto.patient.PatientCreateRequest;
import com.example.drugmed.dto.patient.PatientResponse;
import com.example.drugmed.dto.patient.PatientUpdateRequest;
import com.example.drugmed.dto.patient_referral_letter.PatientReferralLetterResponse;
import com.example.drugmed.dto.prescription.PrescriptionResponse;
import com.example.drugmed.dto.prescription_claim.PrescriptionClaimResponse;
import com.example.drugmed.dto.projection.PatientFullProjection;
import com.example.drugmed.dto.projection.PatientReferralLetterProjection;
import com.example.drugmed.entity.*;
import com.example.drugmed.repository.PatientRepository;
import com.example.drugmed.repository.PrescriptionClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PrescriptionClaimRepository prescriptionClaimRepository;


    public WebResponse<String> createPatient(PatientCreateRequest request){
        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .dateBirth(request.getDateBirth())
                .gender(request.getGender())
                .build();

        patientRepository.save(patient);
        return WebResponse.<String>builder()
                .message("Berhasil Menambahkan Pasien")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<String> updatePatient(Long id, PatientUpdateRequest request) {

        Patient patient = getPatient(id);

        if (request == null || request.getFullName() == null && request.getDateBirth() == null && request.getGender() == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body tidak boleh kosong");
        }

        if (request.getFullName() != null) {
            patient.setFullName(request.getFullName());
        }
        if (request.getDateBirth() != null) {
            patient.setDateBirth(request.getDateBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }

        patientRepository.save(patient);

        return WebResponse.<String>builder()
                .message("Data pasien berhasil di update")
                .status(HttpStatus.OK.value())
                .build();
    }


    public WebResponse<String> deletePatient(long id){
        patientRepository.delete(getPatient(id));
        return WebResponse.<String>builder()
                .message("Berhasil menghapus data pasien")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<List<PatientResponse>> getAllPatient() {
        List<PatientFullProjection> patientList = patientRepository.getAllPatientData();

        if (patientList.isEmpty()) {
            return WebResponse.<List<PatientResponse>>builder()
                    .message("Tidak ada data pasien")
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(Collections.emptyList())
                    .build();
        }

        // Grouping patients
        Map<Long, List<PatientFullProjection>> patientMap = patientList.stream()
                .filter(x -> x.getPatientId() != null)
                .collect(Collectors.groupingBy(PatientFullProjection::getPatientId));

        List<PatientResponse> responses = patientMap.entrySet().stream().map(entry -> {
            Long patientId = entry.getKey();
            List<PatientFullProjection> records = entry.getValue();
            PatientFullProjection firstRecord = records.getFirst();

            // Group referrals
            Map<Long, List<PatientFullProjection>> referralMap = records.stream()
                    .filter(x -> x.getReferralId() != null)
                    .collect(Collectors.groupingBy(PatientFullProjection::getReferralId));
            // group prescription
            Map<Long, List<PatientFullProjection>> prescriptionMap = records.stream()
                    .filter(x -> x.getPrescriptionId() != null )
                    .collect(Collectors.groupingBy(PatientFullProjection::getPrescriptionId));

            List<PatientReferralLetterResponse> letters = referralMap.values().stream().map(referralRecords -> {
                PatientFullProjection referralFirstRecord = referralRecords.getFirst();

                // Group examinations
                Map<Long, List<PatientFullProjection>> examinationMap = referralRecords.stream()
                        .filter(x -> x.getExaminationId() != null)
                        .collect(Collectors.groupingBy(PatientFullProjection::getExaminationId));

                List<ExaminationResponse> examinationResponses = examinationMap.values().stream().map(examRecords -> {
                    PatientFullProjection examFirstRecord = examRecords.getFirst();

                    // Group examination results
                    Map<Long, List<PatientFullProjection>> examinationResultMap = examRecords.stream()
                            .filter(x -> x.getExaminationResultId() != null)
                            .collect(Collectors.groupingBy(PatientFullProjection::getExaminationResultId));

                    List<ExaminationResultResponse> examinationResultResponses = examinationResultMap.values().stream().map(examResRecords -> {
                        PatientFullProjection examResultFirstRecord = examResRecords.getFirst();

                        // Group examination result details
                        Map<Long, List<PatientFullProjection>> examResultDetailMap = examResRecords.stream()
                                .filter(x -> x.getExaminationResultDetailId() != null)
                                .collect(Collectors.groupingBy(PatientFullProjection::getExaminationResultDetailId));

                        List<ExaminationResultDetailResponse> examinationResultDetailResponses = examResultDetailMap.values().stream().map(examResultDetailRecords -> {
                            PatientFullProjection examResultDetailFirstRecord = examResultDetailRecords.getFirst();
                            return ExaminationResultDetailResponse.builder()
                                    .id(examResultDetailFirstRecord.getExaminationResultDetailId())
                                    .examinationResultId(examResultDetailFirstRecord.getExaminationResultId())
                                    .detailResult(examResultDetailFirstRecord.getDetailResult())
                                    .build();
                        }).toList();

                        return ExaminationResultResponse.builder()
                                .id(examResultFirstRecord.getExaminationResultId())
                                .examinerName(examResultFirstRecord.getExaminerName())
                                .resultDetail(examinationResultDetailResponses)
                                .build();
                    }).toList();

                    return ExaminationResponse.builder()
                            .id(examFirstRecord.getExaminationId())
                            .examination(examFirstRecord.getExaminationName())
                            .price(examFirstRecord.getExaminationPrice())
                            .examinationDetail(examFirstRecord.getExaminationDesc())
                            .examinationResults(examinationResultResponses)
                            .createdAt(examFirstRecord.getExaminationCreatedAt())
                            .updatedAt(examFirstRecord.getExaminationUpdatedAt())
                            .build();
                }).toList();

                return PatientReferralLetterResponse.builder()
                        .id(referralFirstRecord.getReferralId())
                        .patientId(referralFirstRecord.getReferralPatientId())
                        .referralDate(referralFirstRecord.getReferralDate())
                        .doctorName(referralFirstRecord.getReferralDoctorName())
                        .verifierName(referralFirstRecord.getVerifierName())
                        .isVerified(
                                referralFirstRecord.getIsVerified() != null
                                        ? PatientReferralLetter.ReferralStatus.valueOf(referralFirstRecord.getIsVerified())
                                        : null
                        )
                        .examinations(examinationResponses)
                        .build();
            }).toList();

            List<PrescriptionResponse> prescription = prescriptionMap.values().stream().map(prescriptionRecords -> {

                PatientFullProjection firstPrecriptionRecord = prescriptionRecords.getFirst();

                // map drug
                Map<Long, List<PatientFullProjection>> drugMap = prescriptionRecords.stream()
                        .filter(x -> x.getDrugId() != null)
                        .collect(Collectors.groupingBy(PatientFullProjection::getDrugId));

                // map claim history
                Map<Long, List<PatientFullProjection>> claimMap = prescriptionRecords.stream()
                        .filter(x -> x.getClaimId() != null)
                        .collect(Collectors.groupingBy(PatientFullProjection::getClaimId));

                List<DrugPatientResponse> drugResponses = drugMap.values().stream().map(
                        drugRecord -> {
                            PatientFullProjection drugFirstRecord = drugRecord.getFirst();

                            Map<Long, List<PatientFullProjection>> drugDetailMap = drugRecord.stream().filter(x -> x.getDrugDetailId() != null)
                                    .collect(Collectors.groupingBy(PatientFullProjection::getDrugDetailId));

                            List<DrugDetailResponse> drugDetailResponses = drugDetailMap.values().stream().map(drugDetailRecord -> {
                                PatientFullProjection drugDetailFirstRecord = drugDetailRecord.getFirst();

                                return DrugDetailResponse.builder()
                                        .id(drugDetailFirstRecord.getDrugDetailId())
                                        .composition(drugDetailFirstRecord.getComposition())
                                        .dosage(drugDetailFirstRecord.getDosage())
                                        .desc(drugDetailFirstRecord.getDescDrug())
                                        .createdAt(drugDetailFirstRecord.getDrugCreatedAt())
                                        .updatedAt(drugDetailFirstRecord.getDrugUpdatedAt())
                                        .build();
                            }).toList();

                            return DrugPatientResponse.builder()
                                    .id(drugFirstRecord.getDrugId())
                                    .drugName(drugFirstRecord.getDrugName())
                                    .category(Drug.DrugCategory.valueOf(drugFirstRecord.getCategory()))
                                    .createdAt(drugFirstRecord.getDrugCreatedAt())
                                    .updatedAt(drugFirstRecord.getDrugUpdatedAt())
                                    .drugDetail(drugDetailResponses.isEmpty() ? null : drugDetailResponses.getFirst())
                                    .build();

                        }).toList();

                List<PrescriptionClaimResponse> claimResponses = claimMap.values().stream().map(
                        claimRecord -> {

                            PatientFullProjection claimHistoryFirstRecord = claimRecord.getFirst();

                            return PrescriptionClaimResponse.builder()
                                    .id(claimHistoryFirstRecord.getClaimId())
                                    .prescriptionId(claimHistoryFirstRecord.getPrescriptionId())
                                    .claimAt(claimHistoryFirstRecord.getClaimAt())
                                    .build();

                        }).toList();


                return PrescriptionResponse.builder()
                        .id(firstPrecriptionRecord.getPrescriptionId())
                        .patientId(firstPrecriptionRecord.getPatientId())
                        .doctorName(firstPrecriptionRecord.getPrescriptionDoctorName())
                        .prescriptionDate(firstPrecriptionRecord.getPrescriptionDate())
                        .createdAt(firstPrecriptionRecord.getPrescriptionCreatedAt())
                        .updatedAt(firstPrecriptionRecord.getPrescriptionUpdatedAt())
                        .drugs(drugResponses)
                        .claimHistory(claimResponses)
                        .build();

            }).toList();

            return PatientResponse.builder()
                    .id(patientId)
                    .fullName(firstRecord.getFullName())
                    .dateBirth(firstRecord.getDateBirth())
                    .gender(
                            firstRecord.getGender() != null
                                    ? Patient.Gender.valueOf(firstRecord.getGender())
                                    : null
                    )
                    .createdAt(firstRecord.getPatientCreatedAt())
                    .updatedAt(firstRecord.getPatientUpdatedAt())
                    .referralLetters(letters)
                    .prescriptions(prescription)
                    .build();
        }).toList();

        return WebResponse.<List<PatientResponse>>builder()
                .message("Berhasil mendapatkan data pasien")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
    }

    public WebResponse<PatientResponse> getPatientById(Long id) {
        Patient patient = getPatient(id);
        List<PatientReferralLetterProjection> letters = patientRepository.getPatientDataById(id);

        List<PatientReferralLetterResponse> referralLetters = letters.isEmpty() ?
                Collections.emptyList() :
                letters.stream()
                        .collect(Collectors.groupingBy(PatientReferralLetterProjection::getReferralId))
                        .values()
                        .stream()
                        .map(this::mapToPatientReferralLetterResponse)
                        .toList();


        PatientResponse responses = PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateBirth(patient.getDateBirth())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .prescriptions(patient
                        .getPrescriptions()
                        .stream()
                        .map(this::mapToPrescriptionResponse)
                        .toList())
                .referralLetters(referralLetters)
                .build();


        return WebResponse.<PatientResponse>builder()
                .message("Berhasil mendapatkan data pasien")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();

    }

    private PatientReferralLetterResponse mapToPatientReferralLetterResponse(List<PatientReferralLetterProjection> records) {
        PatientReferralLetterProjection firstRecord = records.getFirst();

        Map<Long, List<PatientReferralLetterProjection>> examinationMap = records.stream()
                .filter(x -> x.getExaminationId() != null)
                .collect(Collectors.groupingBy(PatientReferralLetterProjection::getExaminationId));

        List<ExaminationResponse> examinations = examinationMap.values().stream()
                .map(this::mapToExaminationResponsePatient)
                .toList();

        return PatientReferralLetterResponse.builder()
                .id(firstRecord.getReferralId())
                .patientId(firstRecord.getPatientId())
                .referralDate(firstRecord.getReferralDate())
                .doctorName(firstRecord.getDoctorName())
                .verifierName(firstRecord.getVerifierName())
                .isVerified(PatientReferralLetter.ReferralStatus.valueOf(firstRecord.getIsVerified()))
                .examinations(examinations)
                .build();
    }

    private ExaminationResponse mapToExaminationResponsePatient(List<PatientReferralLetterProjection> records) {
        PatientReferralLetterProjection firstRecord = records.getFirst();

        Map<Long, List<PatientReferralLetterProjection>> examResultMap = records.stream()
                .filter(x -> x.getExaminationResultId() != null)
                .collect(Collectors.groupingBy(PatientReferralLetterProjection::getExaminationResultId));

        List<ExaminationResultResponse> examResults = examResultMap.values().stream()
                .map(this::mapToExaminationResultResponse)
                .toList();

        return ExaminationResponse.builder()
                .id(firstRecord.getExaminationId())
                .examination(firstRecord.getExaminationName())
                .examinationDetail(firstRecord.getExaminationDesc())
                .price(firstRecord.getPrice())
                .createdAt(firstRecord.getExaminationCreatedAt())
                .updatedAt(firstRecord.getExaminationUpdateAt())
                .examinationResults(examResults)
                .build();
    }

    private ExaminationResultDetailResponse mapToExaminationResultDetailResponse(List<PatientReferralLetterProjection> records) {
        PatientReferralLetterProjection firstRecord = records.getFirst();

        return ExaminationResultDetailResponse.builder()
                .id(firstRecord.getResultDetailId())
                .examinationResultId(firstRecord.getExaminationResultId())
                .detailResult(firstRecord.getDetailResult())
                .build();
    }


    private ExaminationResultResponse mapToExaminationResultResponse(List<PatientReferralLetterProjection> records) {
        PatientReferralLetterProjection firstRecord = records.getFirst();

        Map<Long, List<PatientReferralLetterProjection>> examDetailResultMap = records.stream()
                .filter(x -> x.getResultDetailId() != null)
                .collect(Collectors.groupingBy(PatientReferralLetterProjection::getResultDetailId));

        List<ExaminationResultDetailResponse> resultDetails = examDetailResultMap.values().stream()
                .map(this::mapToExaminationResultDetailResponse)
                .toList();

        return ExaminationResultResponse.builder()
                .id(firstRecord.getExaminationResultId())
                .resultDate(firstRecord.getResultDate()
                )
                .examinerName(firstRecord.getExaminerName())
                .resultDetail(resultDetails)
                .build();
    }

    private PrescriptionResponse mapToPrescriptionResponse(Prescription prescription) {
        List<PrescriptionClaim> claim = prescriptionClaimRepository.findByPrescriptionId(prescription.getId());

        List<PrescriptionClaimResponse> responses = claim.stream().map(x ->
                 PrescriptionClaimResponse.builder()
                         .id(x.getId())
                         .prescriptionId(x.getPrescription().getId())
                         .claimAt(x.getClaimAt())
                         .build()
                ).toList();

        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatient().getId())
                .doctorName(prescription.getDoctorName())
                .prescriptionDate(prescription.getPrescriptionDate())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .drugs(prescription.getDrugs().stream().map(this::mapToDrugPatientResponse).toList())
                .claimHistory(responses)
                .build();
    }



    private DrugPatientResponse mapToDrugPatientResponse(Drug drug) {
        return DrugPatientResponse.builder()
                .id(drug.getId())
                .drugName(drug.getDrugName())
                .category(drug.getCategory())
                .createdAt(drug.getCreatedAt())
                .updatedAt(drug.getUpdatedAt())
                .drugDetail(drug.getDrugDetail() != null ? mapToDrugDetail(drug.getDrugDetail()) : null)
                .build();
    }

    private DrugDetailResponse mapToDrugDetail(DrugDetail detail){
        return DrugDetailResponse.builder()
                .id(detail.getId())
                .composition(detail.getComposition())
                .dosage(detail.getDosage())
                .desc(detail.getDesc())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .build();
    }


    private Patient getPatient(Long id) {
      return patientRepository.findById(id).orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Patient tidak ditemukan"));
    }

}
