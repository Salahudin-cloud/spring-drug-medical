package com.example.drugmed.repository;

import com.example.drugmed.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    @Query("SELECT d FROM Drug d LEFT JOIN FETCH d.drugDetail")
    List<Drug> findAllWithDrugDetail();


    @Query("SELECT d FROM Drug d LEFT JOIN FETCH d.drugDetail WHERE d.id = :id")
    Optional<Drug> finDrugWithDetailById(@Param("id") Long id);

}
