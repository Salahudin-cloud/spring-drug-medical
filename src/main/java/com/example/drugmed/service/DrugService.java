package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug.DrugCreateRequest;
import com.example.drugmed.dto.drug.DrugResponse;
import com.example.drugmed.dto.drug.DrugUpdateRequest;
import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.entity.Drug;
import com.example.drugmed.entity.DrugDetail;
import com.example.drugmed.repository.DrugDetailRepository;
import com.example.drugmed.repository.DrugRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DrugService {

    private final DrugRepository drugRepository;
    private final DrugDetailRepository drugDetailRepository;


    public WebResponse<String> createDrug(DrugCreateRequest request) {
        Drug drug = Drug.builder()
                .drugName(request.getDrugName())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        drug = drugRepository.save(drug);

        DrugDetail detail = DrugDetail.builder()
                .drug(drug)
                .composition(request.getComposition())
                .dosage(request.getDosage())
                .desc(request.getDescription())
                .build();

        drugDetailRepository.save(detail);

        return WebResponse.<String>builder()
                .message("Data obat berhasil di buat")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<String> updateDrug(Long id , DrugUpdateRequest request) {
        Drug drug = getDrug(id);

        if (request == null || (new ObjectMapper().convertValue(request, Map.class)).isEmpty()) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body tidak boleh kosong");
        }

        if (request.getDrugName() != null) {
            drug.setDrugName(request.getDrugName());
        }
        if (request.getCategory() != null) {
            drug.setCategory(request.getCategory());
        }
        if (request.getPrice() != null) {
            drug.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            drug.setStock(request.getStock());
        }

        drugRepository.save(drug);

        return WebResponse.<String>builder()
                .message("Data obat berhasil di update")
                .status(HttpStatus.OK.value())
                .build();
    }


    public WebResponse<String> deleteDrug(Long id) {

        Drug drug = getDrug(id);
        drugRepository.delete(drug);

        return WebResponse.<String>builder()
                .message("Data obat berhasil di hapus")
                .status(HttpStatus.OK.value())
                .build();
    }

        public WebResponse<List<DrugResponse>> getAllDrugDetail() {
            List<Drug> drugs = drugRepository.findAllWithDrugDetail();

            NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

            Currency currency = Currency.getInstance("IDR");
            formatter.setCurrency(currency);



            List<DrugResponse> drugResponses = drugs.stream().map(drug -> {
                Number number;
                try {
                    number = formatter.parse(String.valueOf(drug.getPrice()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                int initValue  = number.intValue();

                return DrugResponse.builder()
                        .id(drug.getId())
                        .drugName(drug.getDrugName())
                        .category(drug.getCategory())
                        .price(initValue)
                        .stock(drug.getStock())
                        .createdAt(drug.getCreatedAt())
                        .updatedAt(drug.getUpdatedAt())
                        .drugDetail(drug.getDrugDetail() != null ? mapToDrugDetailResponse(drug.getDrugDetail()) : null)
                        .build();
            }).collect(Collectors.toList());

            return WebResponse.<List<DrugResponse>>builder()
                    .message("Data obat berhasil di hapus")
                    .status(HttpStatus.OK.value())
                    .data(drugResponses)
                    .build();
        }


    public WebResponse<DrugResponse> getDrugById(Long id) {

        Drug drug = drugRepository.finDrugWithDetailById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obat dengan id " + id + " tidak ditemukan"));

        NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

        Currency currency = Currency.getInstance("IDR");
        formatter.setCurrency(currency);

        Number number;
        try {
            number = formatter.parse(String.valueOf(drug.getPrice()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int initValue  = number.intValue();


        DrugResponse response = DrugResponse.builder()
                .id(drug.getId())
                .drugName(drug.getDrugName())
                .category(drug.getCategory())
                .price(initValue)
                .stock(drug.getStock())
                .createdAt(drug.getCreatedAt())
                .updatedAt(drug.getUpdatedAt())
                .drugDetail(drug.getDrugDetail() != null ? mapToDrugDetailResponse(drug.getDrugDetail()) : null)
                .build();

        return WebResponse.<DrugResponse>builder()
                .message("Berhasi mendapatkan data obat")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }


    private DrugDetailResponse mapToDrugDetailResponse(DrugDetail drugDetail) {
        return DrugDetailResponse.builder()
                .composition(drugDetail.getComposition())
                .dosage(drugDetail.getDosage())
                .desc(drugDetail.getDesc())
                .createdAt(drugDetail.getCreatedAt())
                .updatedAt(drugDetail.getUpdatedAt())
                .build();
    }


    private Drug getDrug(Long id) {
        return drugRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obat dengan id " + id + " tidak ditemukan"));
    }

}
