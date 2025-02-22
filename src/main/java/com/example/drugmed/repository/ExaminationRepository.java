package com.example.drugmed.repository;


import com.example.drugmed.entity.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepository  extends JpaRepository<Examination, Long> {



}
