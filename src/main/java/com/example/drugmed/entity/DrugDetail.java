package com.example.drugmed.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "drug_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrugDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", referencedColumnName = "id", nullable = false)
    private Drug drug;

    @Column(nullable = false)
    private String composition;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false, name = "desc_drug")
    private String desc;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

