package com.example.drugmed.service;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.repository.ExaminationResultDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExaminationResultDetailService {

    private final ExaminationResultDetailRepository examinationResultDetailRepository;


}
