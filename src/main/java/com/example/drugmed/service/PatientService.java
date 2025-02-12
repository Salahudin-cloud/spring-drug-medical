package com.example.drugmed.service;


import com.example.drugmed.dto.WebResponse;
import com.example.drugmed.dto.drug.DrugPatientResponse;
import com.example.drugmed.dto.drug.DrugResponse;
import com.example.drugmed.dto.drug_detail.DrugDetailResponse;
import com.example.drugmed.dto.patient.PatientCreateRequest;
import com.example.drugmed.dto.patient.PatientResponse;
import com.example.drugmed.dto.patient.PatientUpdateRequest;
import com.example.drugmed.dto.prescription.PrescriptionResponse;
import com.example.drugmed.entity.Drug;
import com.example.drugmed.entity.DrugDetail;
import com.example.drugmed.entity.Patient;
import com.example.drugmed.entity.Prescription;
import com.example.drugmed.repository.PatientRepository;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public WebResponse<String> createPatient(PatientCreateRequest request){
        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .dateBirth(request.getDateBirth())
                .gender(request.getGender())
                .build();

        patientRepository.save(patient);
        return WebResponse.<String>builder()
                .message("Berhasil Menambahkan Pasien")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<String> updatePatient(Long id, PatientUpdateRequest request) {

        Patient patient = getPatient(id);

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body tidak boleh kosong");
        }

        if (request.getFullName() != null) {
            patient.setFullName(request.getFullName());
        }
        if (request.getDateBirth() != null) {
            patient.setDateBirth(request.getDateBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }

        patientRepository.save(patient);

        return WebResponse.<String>builder()
                .message("Data pasient berhasil di update")
                .status(HttpStatus.OK.value())
                .build();
    }


    public WebResponse<String> deletePatient(long id){
        patientRepository.delete(getPatient(id));
        return WebResponse.<String>builder()
                .message("Berhasil menghapus data pasien")
                .status(HttpStatus.OK.value())
                .build();
    }

    public WebResponse<List<PatientResponse>> getAllPatient() {
        List<Patient> patient = patientRepository.findAll();

        List<PatientResponse> responses = patient.stream().map(
                x -> PatientResponse.builder()
                        .id(x.getId())
                        .fullName(x.getFullName())
                        .dateBirth(x.getDateBirth())
                        .gender(x.getGender())
                        .createdAt(x.getCreatedAt())
                        .updatedAt(x.getUpdatedAt())
                        .prescription(x.getPrescriptions().stream()
                                .map(this::mapToPrescriptionResponse)
                                .toList()
                        )
                        .build()
        ).toList();

        return WebResponse.<List<PatientResponse>>builder()
                .message("Berhasil Mendapatkan data pasien")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();
    }

    public WebResponse<PatientResponse> getPatientById(Long id) {
        Patient patient = getPatient(id);

        PatientResponse responses = PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateBirth(patient.getDateBirth())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .prescription(patient
                        .getPrescriptions()
                        .stream()
                        .map(this::mapToPrescriptionResponse)
                        .toList())
                .build();


        return WebResponse.<PatientResponse>builder()
                .message("Berhasil mendapatkan data pasien")
                .status(HttpStatus.OK.value())
                .data(responses)
                .build();

    }

    private PrescriptionResponse mapToPrescriptionResponse(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .patient_id(prescription.getPatient().getId())
                .doctorName(prescription.getDoctorName())
                .prescriptionDate(prescription.getPrescriptionDate())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .drugs(prescription.getDrugs().stream().map(this::mapToDrugPatientResponse).toList())
                .build();
    }


    private DrugPatientResponse mapToDrugPatientResponse(Drug drug) {
        return DrugPatientResponse.builder()
                .id(drug.getId())
                .drugName(drug.getDrugName())
                .category(drug.getCategory())
                .createdAt(drug.getCreatedAt())
                .updatedAt(drug.getUpdatedAt())
                .drugDetail(drug.getDrugDetail() != null ? mapToDrugDetail(drug.getDrugDetail()) : null)
                .build();
    }

    private DrugDetailResponse mapToDrugDetail(DrugDetail detail){
        return DrugDetailResponse.builder()
                .id(detail.getId())
                .composition(detail.getComposition())
                .dosage(detail.getDosage())
                .desc(detail.getDesc())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .build();
    }

    private Patient getPatient(Long id) {
      return patientRepository.findById(id).orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Patient tidak ditemukan"));
    }

}
