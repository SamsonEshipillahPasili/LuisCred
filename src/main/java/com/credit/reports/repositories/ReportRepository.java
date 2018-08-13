package com.credit.reports.repositories;

import com.credit.reports.entities.Report;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, String> {
}
