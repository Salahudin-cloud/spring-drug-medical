package com.example.drugmed.repository;

import com.example.drugmed.entity.ListExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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



}
