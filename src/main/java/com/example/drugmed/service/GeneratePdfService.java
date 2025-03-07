package com.example.drugmed.service;

import com.example.drugmed.dto.projection.PatientReferralLetterProjection;
import com.example.drugmed.entity.*;
import com.example.drugmed.repository.PatientRepository;
import com.example.drugmed.repository.PrescriptionClaimRepository;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GeneratePdfService {

    private final PatientRepository patientRepository;
    private final PrescriptionClaimRepository prescriptionClaimRepository;
    private static final String REGULAR = "src/main/resources/fonts/times_new_roman.ttf";
    private static final String BOLD = "src/main/resources/fonts/times_new_roman_bold.ttf";

    public byte[] generatePdf(Long patient_id) throws IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        PdfFont bold = PdfFontFactory.createFont(BOLD);
        PdfFont regular = PdfFontFactory.createFont(REGULAR);

        Patient patient = patientRepository.findById(patient_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient dengan id " + patient_id + " tidak ditemukan"));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            pdfDoc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Laporan Pasien")
                    .setFont(bold)
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER));


            document.add(new Paragraph("Detail Pasien").setFont(bold).setFontSize(18));
            document.add(new Paragraph("Nama: " + patient.getFullName()).setFont(regular).setFontSize(12));
            document.add(new Paragraph("Tanggal Lahir: " + patient.getDateBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).setFont(regular).setFontSize(12));
            document.add(new Paragraph("Jenis Kelamin: " + (patient.getGender() == Patient.Gender.MALE ? "Laki-Laki" : "Perempuan"))).setFont(regular).setFontSize(12);
            document.add(new Paragraph().setMarginBottom(12));


            List<PatientReferralLetterProjection> letters = patientRepository.getPatientDataById(patient_id);


            if (!letters.isEmpty()) {
                Map<Long, List<PatientReferralLetterProjection>> groupedLetters = letters.stream().collect(Collectors.groupingBy(PatientReferralLetterProjection::getReferralId));

                document.add(new Paragraph("Daftar Surat Pengantar").setFont(bold).setFontSize(18));

                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); // IDR format
                currencyFormat.setMaximumFractionDigits(0);
                int referralId = 1;

                for (Map.Entry<Long, List<PatientReferralLetterProjection>> entry : groupedLetters.entrySet()) {
                    Long referralIds = entry.getKey();
                    List<PatientReferralLetterProjection> examinations = entry.getValue();
                    PatientReferralLetterProjection firstLetter = examinations.getFirst();


                    String verificationStatus = (firstLetter.getIsVerified() != null && !firstLetter.getIsVerified().trim().isEmpty())
                            ? firstLetter.getIsVerified().toLowerCase()
                            : "NOT_VERIFIED";

                    document.add(new Paragraph("Surat Pengantar ke " + referralId++).setFont(bold).setFontSize(18));
                    document.add(new Paragraph("Nama Dokter: " + firstLetter.getDoctorName()).setFont(regular).setFontSize(12));
                    document.add(new Paragraph("Verifier: " + (firstLetter.getVerifierName() != null ? firstLetter.getVerifierName() : "Belum diverifikasi")).setFont(regular).setFontSize(12));

                    // Tambahkan status dengan warna background kuning
                    Paragraph statusParagraph = new Paragraph();
                    statusParagraph.add(new Text("Status: ").setFont(regular).setFontSize(12));
                    statusParagraph.add(new Text(verificationStatus) // <-- PERBAIKAN: gunakan variabel tanpa tanda kutip
                            .setFont(regular)
                            .setFontSize(12)
                            .setBackgroundColor(new DeviceRgb(255, 255, 0)));
                    document.add(statusParagraph);

                    document.add(new Paragraph("Tanggal Surat Pengantar: " + firstLetter.getReferralDate().format(dateFormatter)).setFont(regular).setFontSize(12));
                    document.add(new Paragraph().setMarginBottom(12));

                    float[] columnWidthsReferral = {150f, 150f, 200f};
                    Table tableReferral = new Table(columnWidthsReferral);
                    tableReferral.setWidth(new UnitValue(UnitValue.createPercentValue(100)));

                    tableReferral.addCell(new Cell().add(new Paragraph("Nama Pemeriksaan")).setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    tableReferral.addCell(new Cell().add(new Paragraph("Harga")).setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    tableReferral.addCell(new Cell().add(new Paragraph("Deskripsi")).setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                    for (PatientReferralLetterProjection exam : examinations) {
                        tableReferral.addCell(new Cell().add(new Paragraph(exam.getExaminationName())).setFont(regular).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                        tableReferral.addCell(new Cell().add(new Paragraph(currencyFormat.format(exam.getPrice()))).setFont(regular).setFontSize(12).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
                        tableReferral.addCell(new Cell().add(new Paragraph(exam.getExaminationDesc()).setTextAlignment(TextAlignment.JUSTIFIED).setPadding(2)).setFont(regular).setFontSize(12));
                    }
                    document.add(tableReferral);
                    document.add(new Paragraph().setMarginBottom(12));
                }

            } else {
                document.add(new Paragraph("Tidak ada surat pengantar untuk pasien ini.").setFont(regular).setFontSize(12));
            }


            document.add(new Paragraph("Daftar Resep").setFont(bold).setFontSize(18));

            float[] columnWidthsPrescription = {90f, 70f, 90f, 90f, 150f};


            int prescriptionId = 1;
            for (Prescription prescription : patient.getPrescriptions()) {
                document.add(new Paragraph("Resep ke-" + prescriptionId++)
                        .setFont(bold).setFontSize(16));
                document.add(new Paragraph("Nama Dokter: " + prescription.getDoctorName()));
                document.add(new Paragraph("Tanggal Resep: " + prescription.getPrescriptionDate().format(dateFormatter)));

                // Buat tabel baru untuk setiap resep
                Table tablePrescription = new Table(columnWidthsPrescription);
                tablePrescription.setWidth(new UnitValue(UnitValue.createPercentValue(100)));

                // Header tabel
                tablePrescription.addCell(new Cell().add(new Paragraph("Nama Obat"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablePrescription.addCell(new Cell().add(new Paragraph("Kategori"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablePrescription.addCell(new Cell().add(new Paragraph("Komposisi"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablePrescription.addCell(new Cell().add(new Paragraph("Dosis"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tablePrescription.addCell(new Cell().add(new Paragraph("Deskripsi Obat"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));

                // Loop untuk menambahkan obat ke dalam tabel
                for (Drug drug : prescription.getDrugs()) {
                    tablePrescription.addCell(new Cell().add(new Paragraph(drug.getDrugName()))
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFont(regular).setFontSize(12));
                    tablePrescription.addCell(new Cell().add(new Paragraph(String.valueOf(drug.getCategory()).toLowerCase()))
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFont(regular).setFontSize(12));
                    tablePrescription.addCell(new Cell().add(new Paragraph(drug.getDrugDetail().getComposition()))
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFont(regular).setFontSize(12));
                    tablePrescription.addCell(new Cell().add(new Paragraph(drug.getDrugDetail().getDosage()))
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFont(regular).setFontSize(12));
                    tablePrescription.addCell(new Cell().add(new Paragraph(drug.getDrugDetail().getDesc()))
                            .setTextAlignment(TextAlignment.JUSTIFIED)
                            .setFont(regular).setFontSize(12)
                            .setPadding(2));
                }

                // Tambahkan tabel ke dalam dokumen setelah semua obat dalam resep diproses
                document.add(tablePrescription);
                document.add(new Paragraph().setMarginBottom(12)); // Beri jarak antar resep
            }



            document.add(new Paragraph("Riwayat Klaim Obat").setFont(bold).setFontSize(18));



            int claimNo = 1;
            for (Prescription prescription : patient.getPrescriptions()) {
                document.add(new Paragraph("Riwayat Klaim Obat Resep " + prescription.getId()).setFont(bold).setFontSize(14));

                List<PrescriptionClaim> prescriptionClaims = prescriptionClaimRepository.findByPrescriptionId(prescription.getId());

                // Buat tabel baru untuk setiap resep
                Table tableClaim = new Table(new float[]{100f, 100f});
                tableClaim.setWidth(new UnitValue(UnitValue.createPercentValue(100)));

                // Header tabel
                tableClaim.addCell(new Cell().add(new Paragraph("No"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tableClaim.addCell(new Cell().add(new Paragraph("Nama Obat"))
                        .setFont(bold).setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                tableClaim.setKeepTogether(true);


                if (prescriptionClaims.isEmpty()) {
                    tableClaim.addCell(new Cell(1,2).add(new Paragraph("Belum pernah klaim obat").setTextAlignment(TextAlignment.CENTER)).setFont(regular).setFontSize(12));
                }else {
                    for (PrescriptionClaim claim : prescriptionClaims) {
                        tableClaim.addCell(new Cell().add(new Paragraph(String.valueOf(claimNo++)).setTextAlignment(TextAlignment.CENTER).setFont(regular).setFontSize(12)));
                        tableClaim.addCell(new Cell().add(new Paragraph(claim.getClaimAt().format(dateFormatter)).setTextAlignment(TextAlignment.CENTER).setFont(regular).setFontSize(12)));
                    }
                }
                document.add(tableClaim);
                document.add(new Paragraph().setMarginBottom(12));
            }
            document.close();

            PdfDocument modifiedPdfDoc = new PdfDocument(
                    new PdfReader(new ByteArrayInputStream(outputStream.toByteArray())),
                    new PdfWriter(outputStream)
            );
            Document documentLast = new Document(modifiedPdfDoc);

            int lastPageNumber = modifiedPdfDoc.getNumberOfPages();
            PdfPage lastPage = modifiedPdfDoc.getPage(lastPageNumber);

        // Ukuran dan margin QR Code
            float qrSize = 80;
            float marginRight = 20;
            float marginBottom = 20;

            float qrX = PageSize.A4.getWidth() - qrSize - marginRight - 10;
            float qrY = marginBottom;

        // Periksa apakah ada cukup ruang di halaman terakhir
            float availableHeight = lastPage.getPageSize().getHeight() - marginBottom;
            if (availableHeight < qrSize + 30) {
                // Tidak cukup ruang, buat halaman baru
                lastPage = modifiedPdfDoc.addNewPage();
                lastPageNumber = modifiedPdfDoc.getNumberOfPages(); // Update nomor halaman terakhir
                lastPage = modifiedPdfDoc.getPage(lastPageNumber);
            }

        // Membuat QR Code
            BarcodeQRCode qrCode = new BarcodeQRCode(
                    "Di validasi oleh " + patient.getPrescriptions().getFirst().getDoctorName() +
                            ", pada tanggal " + LocalDateTime.now().format(dateFormatter)
            );
            Image qrImage = new Image(qrCode.createFormXObject(ColorConstants.BLACK, modifiedPdfDoc));

        // Menempatkan QR Code pada halaman terakhir
            qrImage.setFixedPosition(qrX, qrY);
            qrImage.scaleToFit(qrSize, qrSize);

        // Menggunakan Canvas agar elemen benar-benar masuk ke halaman terakhir
            Canvas canvas = new Canvas(new PdfCanvas(lastPage),  lastPage.getPageSize());
            canvas.add(qrImage);

        // Menampilkan teks "Di validasi oleh" di atas QR Code dengan Canvas
            canvas.showTextAligned(new Paragraph("Di validasi oleh")
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                            .setFontSize(10),
                    qrX + (qrSize / 2), // Tengah QR Code
                    qrY + qrSize + 10, // Sedikit di atas QR Code
                    TextAlignment.CENTER
            );

            modifiedPdfDoc.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
