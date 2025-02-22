package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "examination_result")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExaminationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "result_date", nullable = false)
    private LocalDateTime resultDate;

    @Column(nullable = false, name = "examiner_name")
    private String examinerName;

    @OneToMany(mappedBy = "examinationResult", cascade = CascadeType.ALL)
    private List<ListExamination> listExaminations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "examinationResult", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ExaminationResultDetail> details;

}
