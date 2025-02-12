package com.example.drugmed.service;

import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailUpdateRequest;
import com.example.drugmed.entity.DrugDetail;
import com.example.drugmed.repository.DrugDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DrugDetailService {

    private final DrugDetailRepository drugDetailRepository;


    public WebResponse<List<DrugDetailResponse>> getAllDrugDetail() {
        List<DrugDetail>  drugDetails = drugDetailRepository.findAll();

        List<DrugDetailResponse> responses = drugDetails.stream().map(
                x -> DrugDetailResponse.builder()
                        .id(x.getId())
                        .composition(x.getComposition())
                        .dosage(x.getDosage())
                        .desc(x.getDesc())
                        .createdAt(x.getCreatedAt())
                        .updatedAt(x.getUpdatedAt())
                        .build()
        ).toList();

        return WebResponse.<List<DrugDetailResponse>> builder()
                .message("Data detail obat berhasil didapatkan")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
    }

    public WebResponse<DrugDetailResponse> getDrugDetailById(Long id) {
        DrugDetail drugDetail = getDrugDetailId(id);

        return WebResponse.<DrugDetailResponse>builder()
                .message("Data detail obat berhasil di dapatkan")
                .status(HttpStatus.OK.value())
                .data(mapToDetaiLDrugResponse(drugDetail))
                .build();
    }

    public WebResponse<Void> updateDrugDetail(Long id, DrugDetailUpdateRequest request) {

        DrugDetail drugDetail = getDrugDetailId(id);

        System.out.println("Composition : " + request.getComposition());
        System.out.println("Dosage : " + request.getComposition());
        System.out.println("Desc : " + request.getComposition());

        if (request.getComposition() != null) {
            drugDetail.setComposition(request.getComposition());
        }
        if (request.getDosage() != null) {
            drugDetail.setDosage(request.getDosage());
        }
        if (request.getDesc() != null) {
            drugDetail.setDesc(request.getDesc());
        }

        drugDetailRepository.save(drugDetail);

        return WebResponse.<Void>builder()
                .message("Data detail obat berhasil diupdate")
                .status(HttpStatus.OK.value())
                .build();
    }

    private DrugDetail getDrugDetailId(Long id){
        return  drugDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail obat dengan id " + id + " tidak ditemukan"));
    }


    public DrugDetailResponse mapToDetaiLDrugResponse(DrugDetail drugDetail) {
        return  DrugDetailResponse.builder()
                .id(drugDetail.getId())
                .composition(drugDetail.getComposition())
                .dosage(drugDetail.getDosage())
                .desc(drugDetail.getDesc())
                .createdAt(drugDetail.getCreatedAt())
                .updatedAt(drugDetail.getUpdatedAt())
                .build();
    }


}
