package com.example.drugmed.repository;

import com.example.drugmed.entity.Examination;
import com.example.drugmed.entity.ListExamination;
import com.example.drugmed.entity.PatientReferralLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListExaminationRepository extends JpaRepository<ListExamination, Long> {
    @Query(value =
            """
            SELECT le.*, prl.* 
            FROM list_examination le 
            JOIN patient_referral_letter prl ON le.referral_letter_id = prl.id
            WHERE le.referral_letter_id = :referralId
            """,
            nativeQuery = true)
    Optional<List<ListExamination>> listExaminationsByReferralId(@Param("referralId") Long referralId);

    @Query(value =
            """
            SELECT le.*, prl.* 
            FROM list_examination le 
            JOIN patient_referral_letter prl ON le.referral_letter_id = prl.id
            WHERE le.referral_letter_id = :referralId
            """,
            nativeQuery = true)
    Optional<ListExamination> findByReferralLetterId(Long referralLetterId);


    @Query(value =
            """
            SELECT COUNT(*) > 0
            FROM list_examination le 
            WHERE le.examination_id = :exam_id
            """,
            nativeQuery = true)
    boolean existsByExaminationId(@Param("exam_id") Long examId);

    @Query(value =
            """
            SELECT * 
            FROM list_examination le 
            WHERE le.examination_id = :examId 
            AND le.referral_letter_id = :referralId
            """,
            nativeQuery = true)
    Optional<ListExamination>findByExaminationIdAndReferralId(@Param("examId") Long examId, @Param("referralId") Long referralId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ListExamination le WHERE le.examination = :examination AND le.referralLetter = :referralLetter")
    void deleteByExaminationAndReferralLetter(@Param("examination") Examination examination, @Param("referralLetter") PatientReferralLetter referralLetter);


    @Query(value = "SELECT EXISTS(SELECT 1 FROM list_examination WHERE examination_id = :examinationId AND referral_letter_id = :referralId)", nativeQuery = true)
    boolean existsByExaminationIdAndReferralId(@Param("examinationId") Long examinationId, @Param("referralId") Long referralId);

    @Query(value =
    """ 
    SELECT * FROM list_examination le 
    WHERE le.referral_letter_id = :referralLetterId
    """, nativeQuery = true)
    List<ListExamination> getAllListExamination(@Param("referralLetterId") Long referralLetter);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM list_examination WHERE referral_letter_id = :referralId", nativeQuery = true)
    void deleteAllListExaminationByReferralId(@Param("referralId") Long referralId);

    @Query("SELECT COUNT(le) > 0 FROM ListExamination le WHERE le.referralLetter.id = :referralId AND le.examination.id = :examinationId")
    boolean existsByReferralLetterAndExamination(@Param("referralId") Long referralId, @Param("examinationId") Long examinationId);


    @Modifying
    @Transactional
    @Query("UPDATE ListExamination le SET le.examinationResult = NULL WHERE le.examinationResult.id = :resultId")
    void updateExaminationResultToNull(@Param("resultId") Long resultId);

}
