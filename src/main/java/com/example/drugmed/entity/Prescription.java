package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToMany
    @JoinTable(
            name = "prescription_drugs",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "drug_id")
    )
    private List<Drug> drugs;

    @Column(nullable = false)
    private String doctorName;


    @Enumerated(EnumType.STRING)
    private ClaimLimit claim;

    @Column(nullable = false, name = "prescription_date")
    private LocalDateTime prescriptionDate;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ClaimLimit{
        ONE_TIMES, TWO_TIMES, THREE_TIMES
    }
}

