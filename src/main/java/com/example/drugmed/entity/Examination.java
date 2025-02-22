package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "examination")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String examination;

    @Column(nullable = false)
    private String examinationDesc;

    @Column(nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "examination", cascade = CascadeType.ALL)
    private List<ListExamination> listExaminations;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
