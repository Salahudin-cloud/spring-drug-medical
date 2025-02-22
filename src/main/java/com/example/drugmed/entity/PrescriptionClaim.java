package com.example.drugmed.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescription_claim")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @Column(nullable = false, name = "claim_at")
    private LocalDateTime claimAt;

}
