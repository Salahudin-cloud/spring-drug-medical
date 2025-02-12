package com.example.drugmed.repository;

import com.example.drugmed.entity.DrugDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DrugDetailRepository extends JpaRepository<DrugDetail, Long> {


    @Modifying
    @Transactional
    @Query("UPDATE DrugDetail d SET " +
            "d.composition = CASE WHEN :composition IS NOT NULL THEN :composition ELSE d.composition END, " +
            "d.dosage = CASE WHEN :dosage IS NOT NULL THEN :dosage ELSE d.dosage END, " +
            "d.desc = CASE WHEN :desc IS NOT NULL THEN :desc ELSE d.desc END " +
            "WHERE d.id = :id")
    int updateDrugDetail(@Param("id") Long id,
                         @Param("composition") String composition,
                         @Param("dosage") String dosage,
                         @Param("desc") String desc);
}
