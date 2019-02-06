package com.credit.reports.controllers;

import com.credit.reports.entities.Report;
import com.credit.reports.pdf.PdfGenerator;
import com.credit.reports.pdf.ReportData;
import com.credit.reports.repositories.ReportRepository;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReportTemplateController {
    @Autowired
    private ReportRepository reportRepository;

    @GetMapping({"/report/{reportID}/v1"})
    public String loadReport(Model model, @PathVariable String reportID) {
        try {
            Report[] report = new Report[]{null};
            this.reportRepository.findById(reportID).ifPresent((r) -> report[0] = r);
            if (report[0] == null) {
                return "redirect:/error";
            }

            ReportData reportData = report[0].getReportDataProxy().reportData();
            model.addAttribute("reportData", reportData);
        } catch (Exception var5) {
            Logger.getLogger(ReportTemplateController.class.getName()).log(Level.SEVERE, (String)null, var5);
        }

        return "report_template";
    }

    @GetMapping({"/report/pdf/{reportID}"})
    public ResponseEntity<byte[]> getReportAsPdf(@PathVariable String reportID) throws Exception {
        byte[] pdfBytes;
        Optional<Report> reportOptional = this.reportRepository.findById(reportID);
        if (!reportOptional.isPresent()) {
            return null;
        } else {
            Report report = reportOptional.get();
            pdfBytes = report.getDocumentBytes();
            if (pdfBytes == null || pdfBytes.length == 0) {
                pdfBytes = PdfGenerator.generatePDF("http://localhost:8081/report/" + reportID + "/v1");
                report.setDocumentBytes(pdfBytes);
                this.reportRepository.save(report);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }
    }
}
