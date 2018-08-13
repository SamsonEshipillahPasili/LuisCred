package com.credit.reports.controllers;

import com.credit.reports.dto.ReportDataProxy;
import com.credit.reports.entities.Report;
import com.credit.reports.repositories.ReportRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CreditRestController {
    @Autowired
    private ReportRepository reportRepository;

    public CreditRestController() {
    }

    @PostMapping({"/credit/upload/v1"})
    public String upload(@RequestParam MultipartFile file, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String clientName, @RequestParam String clientAddress) throws IOException {
        if (!file.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !clientName.isEmpty() && !clientAddress.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                fileName = fileName == null ? "" : fileName;
                if (!fileName.endsWith(".html") && !fileName.endsWith(".htm")) {
                    throw new RuntimeException("Invalid File!");
                } else {
                    ReportDataProxy reportDataProxy = new ReportDataProxy();
                    reportDataProxy.setInputHtmlFileBytes(file.getBytes());
                    reportDataProxy.setEmail(email);
                    reportDataProxy.setPhoneNumber(phoneNumber);
                    reportDataProxy.setClientName(clientName);
                    reportDataProxy.setClientAddress(clientAddress);
                    String referenceNumber = reportDataProxy.reportData().getRefNumber();
                    Report report = new Report(referenceNumber, reportDataProxy);
                    referenceNumber = referenceNumber.isEmpty() ? "none" : referenceNumber;
                    this.reportRepository.save(report);
                    return "success:" + referenceNumber;
                }
            } catch (Exception var10) {
                var10.printStackTrace();
                return "error:" + var10.getLocalizedMessage();
            }
        } else {
            return "error:All fields are required!";
        }
    }
}
