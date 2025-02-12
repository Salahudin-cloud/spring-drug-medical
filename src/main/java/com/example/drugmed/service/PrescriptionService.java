package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.prescription.PrescriptionCreateRequest;
import com.example.drugmed.dto.prescription.PrescriptionDrugUpdateRequest;
import com.example.drugmed.dto.prescription.PrescriptionUpdateRequest;
import com.example.drugmed.entity.Drug;
import com.example.drugmed.entity.Patient;
import com.example.drugmed.entity.Prescription;
import com.example.drugmed.repository.DrugRepository;
import com.example.drugmed.repository.PatientRepository;
import com.example.drugmed.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DrugRepository drugRepository;

    public WebResponse<Void> createPrescription(PrescriptionCreateRequest request){
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient tidak ditemukan"));


        List<Drug> drugs = drugRepository.findAllById(request.getDrugIds());


        if (drugs.size() != request.getDrugIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beberapa obat tidak ditemukan dalam database");
        }

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .drugs(drugs)
                .doctorName(request.getDoctorName())
                .prescriptionDate(request.getPrescriptionDate())
                .build();

        prescriptionRepository.save(prescription);

        return WebResponse.<Void>builder()
                .message("Data resep berhasil di buat")
                .status(HttpStatus.OK.value())
                .build();
    }


    public WebResponse<Void> deletePrescriptionById(Long id) {
        Prescription prescription =  getPrescriptionById(id);

        prescription.getDrugs().clear();
        prescriptionRepository.delete(prescription);
        return WebResponse.<Void>builder()
                .message("Data resep berhasil dihapus")
                .status(HttpStatus.OK.value())
                .build();
    }


    public WebResponse<Void> UpdatePrescription(Long id, PrescriptionUpdateRequest request) {
        Prescription prescription = getPrescriptionById(id);

        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pasien dengan id " + request.getPatientId() + " tidak ditemukan"));
            prescription.setPatient(patient);
        }

        if (request.getDrugUpdateRequest() != null && !request.getDrugUpdateRequest().isEmpty()) {
            processDrugUpdates(prescription, request.getDrugUpdateRequest());
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



    private void processDrugUpdates(Prescription prescription, List<PrescriptionDrugUpdateRequest> drugUpdates) {
        for (PrescriptionDrugUpdateRequest drugUpdate : drugUpdates) {
            Drug drug = drugRepository.findById(drugUpdate.getDrugId())
                    .orElseThrow(() -> new RuntimeException("Drug not found"));

            switch (drugUpdate.getAction()) {
                case ADD:
                    if (!prescription.getDrugs().contains(drug)) {
                        prescription.getDrugs().add(drug);
                    }
                    break;

                case DELETE:
                    prescription.getDrugs().remove(drug);
                    break;

                case REPLACE_ALL:
                    prescription.getDrugs().clear();
                    prescription.getDrugs().add(drug);
                    break;

                case REPLACE:
                    if (drugUpdate.getOldDrugId() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Silahkan isi obat yang mau diganti");
                    }

                    Drug oldDrug = drugRepository.findById(drugUpdate.getOldDrugId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obat dengan id " + drugUpdate.getOldDrugId() + " tidak ditemukan"));

                    if (!prescription.getDrugs().contains(oldDrug)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Obat yang ingin diganti tidak ada di dalam resep");
                    }

                    // Remove old drug and add the new one
                    prescription.getDrugs().remove(oldDrug);
                    prescription.getDrugs().add(drug);
                    break;

                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action: " + drugUpdate.getAction());
            }
        }
    }

    private Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prescription with id " + id + " doenst exist"));
    }

}
