package com.example.drugmed.repository;

import com.example.drugmed.dto.projection.PatientReferralLetterProjection;
import com.example.drugmed.entity.PatientReferralLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientReferralLetterRepository extends JpaRepository<PatientReferralLetter, Long> {

    @Query( value = """ 
    SELECT
        prl.id AS referralId,
        prl.patient_id AS patientId,
        prl.doctor_name AS doctorName,
        prl.is_verified AS isVerified,
        prl.verifier_name AS verifierName,
        prl.referral_date AS referralDate,

        e.id AS examinationId,
        e.examination AS examinationName,
        e.examination_desc AS examinationDesc,
        e.price AS price,
        e.created_at AS examinationCreatedAt,
        e.updated_at AS examinationUpdateAt,

        er.id AS examinationResultId,
        er.result_date AS resultDate,
        er.examiner_name AS examinerName,

        erd.id AS resultDetailId,
        erd.examination_result_id AS examinationIdResultDetail,
        erd.detail_result AS detailResult

    FROM patient_referral_letter prl
    LEFT JOIN list_examination le ON prl.id = le.referral_letter_id
    LEFT JOIN examination e ON le.examination_id = e.id  -- FIXED
    LEFT JOIN examination_result er ON le.examination_result_id = er.id
    LEFT JOIN examination_result_detail erd ON er.id = erd.examination_result_id
""", nativeQuery = true)
    List<PatientReferralLetterProjection> getAllReferral();

    @Query(
            value = """
                     SELECT
                    prl.id AS referralId,
                    prl.patient_id AS patientId,
                    prl.doctor_name AS doctorName,
                    prl.is_verified AS isVerified,
                    prl.verifier_name AS verifierName,
                    prl.referral_date AS referralDate,
                    e.id AS examinationId,
                    e.examination AS examinationName,
                    e.examination_desc AS examinationDesc,
                    e.price AS price,
                    e.created_at AS examinationCreatedAt,
                    e.updated_at AS examinationUpdateAt,
                    er.id AS examinationResultId,
                    er.result_date AS resultDate,
                    er.examiner_name AS examinerName,
                    erd.id AS resultDetailId,
                    erd.examination_result_id AS examinationIdResultDetail,
                    erd.detail_result AS detailResult
                    FROM patient_referral_letter prl
                    LEFT JOIN list_examination le ON prl.id = le.referral_letter_id
                    LEFT JOIN examination e ON le.examination_id = e.id
                    LEFT JOIN examination_result er ON le.examination_result_id = er.id
                    LEFT JOIN examination_result_detail erd ON er.id = erd.examination_result_id
                    where prl.patient_id = :patientId
                    """, nativeQuery = true)
    List<PatientReferralLetterProjection> getAllReferralPatientId(@Param("patientId") Long id);

}
