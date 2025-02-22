package com.example.drugmed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_referral_letter")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientReferralLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false, name = "doctor_name")
    private String doctorName;

    @Column(name = "verifier_name")
    private String verifierName;

    @Column(nullable = false, name = "referral_date")
    private LocalDateTime referralDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferralStatus isVerified;

    public enum ReferralStatus {
        VERIFIED, NOT_VERIFIED
    }
}
