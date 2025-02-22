package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "examination_result_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationResultDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_result_id")
    private ExaminationResult examinationResult;

    @Column(nullable = false, name = "detail_result")
    private String detailResult;
}

