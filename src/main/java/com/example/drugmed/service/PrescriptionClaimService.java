package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.prescription_claim.PrescriptionClaimResponse;
import com.example.drugmed.entity.Drug;
import com.example.drugmed.entity.Prescription;
import com.example.drugmed.entity.PrescriptionClaim;
import com.example.drugmed.repository.DrugRepository;
import com.example.drugmed.repository.PrescriptionClaimRepository;
import com.example.drugmed.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PrescriptionClaimService {

    private final PrescriptionClaimRepository prescriptionClaimRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DrugRepository drugRepository;

    @Transactional
    public WebResponse<Void> claimPrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resep obat dengan id " + id + " tidak ditemukan"));

        Long countHowManyTimesClaim = prescriptionClaimRepository.countByPrescriptionId(id);

        switch (prescription.getClaim()) {
            case ONE_TIMES:
                checkLimitClaim(countHowManyTimesClaim, 1);
                break;
            case TWO_TIMES:
                checkLimitClaim(countHowManyTimesClaim, 2);
                break;
            case THREE_TIMES:
                checkLimitClaim(countHowManyTimesClaim, 3);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batas klaim tidak valid");
        }

        for (Drug drug : prescription.getDrugs()){
                if (drug.getStock() > 0) {
                    drug.decreaseDrugStock();
                    drugRepository.save(drug);
                }else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Obat dengan nama " + drug.getDrugName() + " stock telah habis");
                }
        }

        PrescriptionClaim claim = PrescriptionClaim.builder()
                .prescription(prescription)
                .claimAt(LocalDateTime.now())
                .build();

        prescriptionClaimRepository.save(claim);


        return WebResponse.<Void>builder()
                .message("Berhasil claim resep")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<List<PrescriptionClaimResponse>> getAllClaims(){

        List<PrescriptionClaim> response = prescriptionClaimRepository.findAll();

        return WebResponse.<List<PrescriptionClaimResponse>>builder()
                .message("Berhasil mendapatkan data")
                .status(HttpStatus.OK.value())
                .data(response.stream()
                        .map(this::mapToPrescriptionClaimResponse)
                        .toList())
                .build();
    }

    public WebResponse<List<PrescriptionClaimResponse>> getPrescriptionById(Long id) {
        List<PrescriptionClaim> response = prescriptionClaimRepository.findByPrescriptionId(id);

        return WebResponse.<List<PrescriptionClaimResponse>>builder()
                .message("Berhasil mendapatkan data")
                .status(HttpStatus.OK.value())
                .data(response.stream()
                        .map(this::mapToPrescriptionClaimResponse)
                        .toList())
                .build();
    }

    private void checkLimitClaim(Long timesClaim, Integer limitClaims) {
        if (timesClaim >= limitClaims) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Claim resep sudah melebihi batas");
        }
    }


    private PrescriptionClaimResponse mapToPrescriptionClaimResponse(PrescriptionClaim claim) {
        return PrescriptionClaimResponse.builder()
                .id(claim.getId())
                .prescriptionId(claim.getPrescription().getId())
                .claimAt(claim.getClaimAt())
                .build();
    }

}
