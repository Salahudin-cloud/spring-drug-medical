package com.example.drugmed.repository;

import com.example.drugmed.dto.projection.PatientFullProjection;
import com.example.drugmed.dto.projection.PatientReferralLetterProjection;
import com.example.drugmed.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = """
            SELECT
            p.id AS patientId,
            p.full_name AS fullName,
            p.date_birth AS dateBirth,
            p.gender,
            p.created_at AS patientCreatedAt,
            p.updated_at AS patientUpdatedAt,
            p2.id AS prescriptionId,
            p2.patient_id AS prescriptionPatientId,
            p2.doctor_name AS prescriptionDoctorName,
            p2.prescription_date as prescriptionDate,
            p2.created_at AS prescriptionCreatedAt,
            p2.updated_at AS prescriptionUpdatedAt,
            p2.claim,
            pc.id AS claimId,
            pc.claim_at AS claimAt,
            pc.prescription_id AS prescriptionClaimId,
            d.id AS drugId,
            d.category,
            d.drug_name AS drugName,
            d.price,
            d.stock,
            d.created_at AS drugCreatedAt,
            d.updated_at AS drugUpdatedAt,
            dd.id AS drugDetailId,
            dd.drug_id AS drugIdDetail,
            dd.composition,
            dd.dosage,
            dd.desc_drug AS descDrug,
            dd.created_at AS drugDetailCreatedAt,
            dd.updated_at AS drugDetailUpdatedAt,
            prl.id AS referralId,
            prl.patient_id AS referralPatientId,
            prl.doctor_name AS referralDoctorName,
            prl.is_verified AS isVerified,
            prl.verifier_name AS verifierName,
            prl.referral_date AS referralDate,
            e.id as examinationId,
            e.examination AS examinationName,
            e.examination_desc AS examinationDesc,
            e.price AS examinationPrice,
            e.created_at AS examinationCreatedAt,
            e.updated_at AS examinationUpdatedAt,
            er.id AS examinationResultId,
            er.result_date AS resultDate,
            er.examiner_name AS examinerName,
            erd.id AS resultDetailId,
            erd.examination_result_id AS examinationResultDetailId,
            erd.detail_result AS detailResult
            FROM patient p
            LEFT JOIN prescription p2 ON p.id = p2.patient_id
            LEFT JOIN prescription_claim pc ON p2.id = pc.prescription_id
            LEFT JOIN prescription_drugs pd ON p2.id = pd.prescription_id
            LEFT JOIN drug d ON pd.drug_id = d.id
            LEFT JOIN drug_detail dd ON d.id = dd.drug_id
            LEFT JOIN patient_referral_letter prl ON p.id = prl.patient_id
            LEFT JOIN list_examination le ON prl.id = le.referral_letter_id
            LEFT JOIN examination e ON le.examination_id = e.id
            LEFT JOIN examination_result er ON e.id = er.id
            LEFT JOIN examination_result_detail erd ON er.id = erd.examination_result_id;
    """, nativeQuery = true)
    List<PatientFullProjection> getAllPatientData();


    @Query(value = """
            select
            prl.id as referralId,
            prl.patient_id as patientId,
            prl.doctor_name as doctorName,
            prl.is_verified as isVerified,
            prl.verifier_name as verifierName,
            prl.referral_date as referralDate,
            e.id as examinationId,
            e.examination as examinationName,
            e.examination_desc as examinationDesc,
            e.price ,
            e.created_at as examinationCreatedAt,
            e.updated_at as examinationUpdateAt,
            er.id as examinationResultId,
            er.result_date as resultDate,
            er.examiner_name as examinerName,
            erd.id as resultDetailId,
            erd.examination_result_id as examinationIdResultDetail,
            erd.detail_result as detailResult
            from patient_referral_letter prl
            left join list_examination le on prl.id = le.referral_letter_id
            left join examination e on le.examination_id  = e.id
            left join examination_result er on le.examination_result_id = er.id
            left join examination_result_detail erd on er.id = erd.examination_result_id
            where prl.patient_id = :patientId
    """, nativeQuery = true)
    List<PatientReferralLetterProjection> getPatientDataById(@Param("patientId") Long patientId);

}
