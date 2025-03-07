package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug.DrugPatientResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.dto.prescription.PrescriptionCreateRequest;
import com.example.drugmed.dto.prescription.PrescriptionDrugUpdateRequest;
import com.example.drugmed.dto.prescription.PrescriptionResponse;
import com.example.drugmed.dto.prescription.PrescriptionUpdateRequest;
import com.example.drugmed.entity.Drug;
import com.example.drugmed.entity.DrugDetail;
import com.example.drugmed.entity.Patient;
import com.example.drugmed.entity.Prescription;
import com.example.drugmed.repository.DrugRepository;
import com.example.drugmed.repository.PatientRepository;
import com.example.drugmed.repository.PrescriptionClaimRepository;
import com.example.drugmed.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DrugRepository drugRepository;
    private final PrescriptionClaimRepository prescriptionClaimRepository;

    public WebResponse<Void> createPrescription(PrescriptionCreateRequest request){
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pasien tidak ditemukan"));


        List<Drug> drugs = drugRepository.findAllById(request.getDrugIds());


        if (drugs.size() != request.getDrugIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beberapa obat tidak ditemukan dalam database");
        }

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .drugs(drugs)
                .doctorName(request.getDoctorName())
                .claim(request.getClaim())
                .prescriptionDate(request.getPrescriptionDate())
                .build();

        prescriptionRepository.save(prescription);

        return WebResponse.<Void>builder()
                .message("Data resep berhasil di buat")
                .status(HttpStatus.OK.value())
                .build();
    }


    @Transactional
    public WebResponse<Void> deletePrescriptionById(Long id) {
        Prescription prescription = getPrescriptionById(id);

        prescriptionClaimRepository.deleteByPrescriptionId(id);
        prescription.getDrugs().clear();
        prescriptionRepository.delete(prescription);

        return WebResponse.<Void>builder()
                .message("Data resep berhasil dihapus")
                .status(HttpStatus.OK.value())
                .build();
    }


    public WebResponse<Void> UpdatePrescription(Long precriptionId, Long pasienId, PrescriptionUpdateRequest request) {
        Prescription prescription = getPrescriptionById(precriptionId);

        if (pasienId != null) {
            Patient patient = patientRepository.findById(pasienId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pasien dengan id " + pasienId + " tidak ditemukan"));
            prescription.setPatient(patient);
        }

        if (request.getDrug() != null && !request.getDrug().isEmpty()) {
            processDrugUpdates(prescription, request.getDrug());
        }
        if (request.getDoctorName() != null) {
            prescription.setDoctorName(request.getDoctorName());
        }
        if (request.getPrescriptionDate() != null) {
            prescription.setPrescriptionDate(request.getPrescriptionDate());
        }

        prescriptionRepository.save(prescription);

        return WebResponse.<Void>builder()
                .message("Berhasil mengupdate data resep")
                .status(HttpStatus.OK.value())
                .build();

    }

    public WebResponse<List<PrescriptionResponse>> getAllPrescription() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();

        List<PrescriptionResponse> responses = prescriptions.stream().map(
                x -> PrescriptionResponse.builder()
                        .id(x.getId())
                        .patientId(x.getPatient().getId())
                        .doctorName(x.getDoctorName())
                        .prescriptionDate(x.getPrescriptionDate())
                        .createdAt(x.getCreatedAt())
                        .updatedAt(x.getUpdatedAt())
                        .drugs(x.getDrugs()
                                .stream()
                                .map(this::mapToDrugPatientResponse)
                                .toList()
                        )
                        .build()
        ).toList();

        return WebResponse.<List<PrescriptionResponse>>builder()
                .message("Berhasil mendapatkan data resep")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();

    }

    private DrugPatientResponse mapToDrugPatientResponse(Drug drug) {
        return DrugPatientResponse.builder()
                .id(drug.getId())
                .drugName(drug.getDrugName())
                .category(drug.getCategory())
                .createdAt(drug.getCreatedAt())
                .updatedAt(drug.getUpdatedAt())
                .drugDetail(mapToDrugDetailResponse(drug.getDrugDetail()))
                .build();
    }

    private DrugDetailResponse mapToDrugDetailResponse(DrugDetail drugDetail) {
        return DrugDetailResponse.builder()
                .id(drugDetail.getId())
                .composition(drugDetail.getComposition())
                .dosage(drugDetail.getDosage())
                .desc(drugDetail.getDesc())
                .createdAt(drugDetail.getCreatedAt())
                .updatedAt(drugDetail.getUpdatedAt())
                .build();
    }


    private void processDrugUpdates(Prescription prescription, List<PrescriptionDrugUpdateRequest> drugUpdates) {
        List<Long> replaceAllDrugs = new ArrayList<>();

        for (PrescriptionDrugUpdateRequest drugUpdate : drugUpdates) {

            if (drugUpdate.getAction() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Silahkan isi aksi yang mau dilakukan");
            }

            switch (drugUpdate.getAction()) {
                case ADD:
                    Drug drug = drugRepository.findById(drugUpdate.getDrugId())
                            .orElseThrow(() -> new RuntimeException("Drug not found"));
                    if (!prescription.getDrugs().contains(drug)) {
                        prescription.getDrugs().add(drug);
                    }
                    break;

                case DELETE:
                    Drug drugToDelete = drugRepository.findById(drugUpdate.getDrugId())
                            .orElseThrow(() -> new RuntimeException("Drug not found"));
                    prescription.getDrugs().remove(drugToDelete);
                    break;

                case REPLACE:
                    if (drugUpdate.getOldDrugId() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Silahkan isi obat yang mau diganti");
                    }
                    Drug oldDrug = drugRepository.findById(drugUpdate.getOldDrugId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obat dengan id " + drugUpdate.getOldDrugId() + " tidak ditemukan"));
                    Drug newDrug = drugRepository.findById(drugUpdate.getDrugId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obat dengan id " + drugUpdate.getOldDrugId() + " tidak ditemukan"));

                    if (!prescription.getDrugs().contains(oldDrug)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Obat yang ingin diganti tidak ada di dalam resep");
                    }

                    prescription.getDrugs().remove(oldDrug);
                    prescription.getDrugs().add(newDrug);
                    break;

                case REPLACE_ALL:
                    if (drugUpdate.getDrugIds() == null || drugUpdate.getDrugIds().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Silahkan masukkan daftar obat baru");
                    }
                    replaceAllDrugs.addAll(drugUpdate.getDrugIds());
                    break;

                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aksi untuk  " + drugUpdate.getAction() + " tidak tersedia");
            }
        }


        if (!replaceAllDrugs.isEmpty()) {
            prescription.getDrugs().clear();
            for (Long drugId : replaceAllDrugs) {
                Drug drug = drugRepository.findById(drugId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Obat dengan id " + drugId + " tidak ditemukan"));
                prescription.getDrugs().add(drug);
            }
        }
    }


    private Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resep dengan id  " + id + " tidak ditemukan"));
    }

}
