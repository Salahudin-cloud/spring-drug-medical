package com.example.drugmed.dto.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PatientFullProjection {
    // Patient Details
    Long getPatientId();
    String getFullName();
    LocalDate getDateBirth();
    String getGender();
    LocalDateTime getPatientCreatedAt();
    LocalDateTime getPatientUpdatedAt();

    // Prescription
    Long getPrescriptionId();
    Long getPrescriptionPatientId();
    String getPrescriptionDoctorName();
    LocalDateTime getPrescriptionDate();
    LocalDateTime getPrescriptionCreatedAt();
    LocalDateTime getPrescriptionUpdatedAt();
    String getClaim(); // p2.claim

    // Prescription Claim
    Long getClaimId();
    LocalDateTime getClaimAt();
    Long getPrescriptionClaimId();

    // Drug (Obat)
    Long getDrugId();
    String getCategory();
    String getDrugName();
    Integer getPrice();
    Integer getStock();
    LocalDateTime getDrugCreatedAt();
    LocalDateTime getDrugUpdatedAt();

    // Drug Detail (Detail Obat)
    Long getDrugDetailId();
    Long getDrugIdDetail();
    String getComposition();
    String getDosage();
    String getDescDrug();
    LocalDateTime getDrugDetailCreatedAt();
    LocalDateTime getDrugDetailUpdatedAt();

    // Referral Letter (Surat Rujukan)
    Long getReferralId();
    Long getReferralPatientId();
    String getReferralDoctorName();
    String getVerifierName();
    String getIsVerified();
    LocalDateTime getReferralDate();

    // Examination (Pemeriksaan)
    Long getExaminationId();
    String getExaminationName();
    String getExaminationDesc();
    Integer getExaminationPrice();
    LocalDateTime getExaminationCreatedAt();
    LocalDateTime getExaminationUpdatedAt();

    // Examination Result (Hasil Pemeriksaan)
    Long getExaminationResultId();
    LocalDateTime getResultDate();
    String getExaminerName();

    // Examination Result Detail
    Long getResultDetailId();
    Long getExaminationResultDetailId();
    String getDetailResult();
}
