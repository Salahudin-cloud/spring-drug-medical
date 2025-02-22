package com.example.drugmed.dto.projection;

import java.time.LocalDateTime;

public interface PatientReferralLetterProjection {

    Long getReferralId();
    Long getPatientId();
    String getDoctorName();
    String getIsVerified();
    String getVerifierName();
    LocalDateTime getReferralDate();

    Long getExaminationId();
    String getExaminationName();
    String getExaminationDesc();
    Integer getPrice();
    LocalDateTime getExaminationCreatedAt();
    LocalDateTime getExaminationUpdateAt();

    Long getExaminationResultId();
    LocalDateTime getResultDate();
    String getExaminerName();

    Long getResultDetailId();
    Long getExaminationIdResultDetail();
    String getDetailResult();

}
