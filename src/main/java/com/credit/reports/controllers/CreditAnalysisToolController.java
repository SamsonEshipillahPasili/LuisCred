package com.credit.reports.controllers;

import com.credit.reports.entities.CRUser;
import com.credit.reports.entities.Report;
import com.credit.reports.repositories.CRUserRepository;
import com.credit.reports.repositories.ReportRepository;
import com.credit.reports.services.CRUserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CreditAnalysisToolController {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private CRUserService CRUserService;
    @Autowired
    private CRUserRepository crUserRepository;

    public CreditAnalysisToolController() {
    }

    @PostMapping({"/delete_report"})
    public String deleteReport(Model model, @RequestParam String deleteKey) {
        model.addAttribute("reportService", this.reportRepository);
        this.reportRepository.deleteById(deleteKey);
        return "record_history";
    }

    @GetMapping({"/log_in.html"})
    public String logIn() {
        return "log_in";
    }

    @GetMapping({"/log_out.html"})
    public String logOut() {
        return "log_in";
    }

    @GetMapping({"/"})
    public String homePage() {
        return "home";
    }

    @GetMapping({"/dashboard.html"})
    public String dashboard() {
        //CRUser crUser = this.crUserRepository.findById("admin").get();
        return "dashboard";
    }

    @GetMapping({"/record_history.html"})
    public String recordHistory(Model model) {
        model.addAttribute("reportService", this.reportRepository);
        return "record_history";
    }

    @GetMapping({"/clients.html"})
    public String clients() {
        return "clients";
    }

    @GetMapping({"/disputes.html"})
    public String disputes() {
        return "disputes";
    }

    @GetMapping({"/search.html"})
    public String search(Model model, @RequestParam String searchKey) {
        Report[] report = new Report[1];
        this.reportRepository.findById(searchKey).ifPresent((report1) -> {
            report[0] = report1;
        });
        if (null == report[0]) {
            model.addAttribute("results", new ArrayList());
        } else {
            model.addAttribute("results", Arrays.asList(report[0]));
        }

        return "search";
    }

    @PostMapping({"/dashboard.html"})
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        if (newPassword.trim().isEmpty()) {
            return "redirect:/dashboard.html?password_error";
        } else if (!newPassword.trim().equals(confirmPassword.trim())) {
            return "redirect:/dashboard.html?password_error";
        } else {
            return this.CRUserService.updatePassword(oldPassword, newPassword) ? "redirect:/dashboard.html?password_updated" : "redirect:/dashboard.html?password_error";
        }
    }
}
