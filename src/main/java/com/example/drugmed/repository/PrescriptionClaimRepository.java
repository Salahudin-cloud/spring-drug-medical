package com.example.drugmed.repository;

import com.example.drugmed.entity.PrescriptionClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionClaimRepository extends JpaRepository<PrescriptionClaim, Long> {

    Long countByPrescriptionId(Long id);
    List<PrescriptionClaim> findByPrescriptionId(Long prescriptionId);
    Optional<PrescriptionClaim> findFirstByPrescriptionId(Long prescriptionId);
}
