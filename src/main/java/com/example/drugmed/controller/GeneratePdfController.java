package com.example.drugmed.controller;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.service.GeneratePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pdf")
public class GeneratePdfController {

    private final GeneratePdfService generatePdfService;

    @GetMapping(
            path = "/get-patient",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPdf(@RequestParam Long patient_id) throws IOException {
        return generatePdfService.generatePdf(patient_id);
    }


}
