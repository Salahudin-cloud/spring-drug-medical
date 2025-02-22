package com.example.drugmed.repository;

import com.example.drugmed.entity.ExaminationResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationResultDetailRepository extends JpaRepository<ExaminationResultDetail, Long> {
}
