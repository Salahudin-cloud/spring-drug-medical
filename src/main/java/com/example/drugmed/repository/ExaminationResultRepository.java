package com.example.drugmed.repository;

import com.example.drugmed.entity.ExaminationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, Long> {
}
