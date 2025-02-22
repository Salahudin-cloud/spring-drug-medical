package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "list_examination")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListExamination {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "examination_id", nullable = false)
    private Examination examination;

    @ManyToOne
    @JoinColumn(name = "referral_letter_id", nullable = false)
    private PatientReferralLetter referralLetter;

    @ManyToOne
    @JoinColumn(name = "examination_result_id" )
    private ExaminationResult examinationResult;


}
