package com.credit.reports.repositories;

import com.credit.reports.entities.CRUser;
import org.springframework.data.repository.CrudRepository;

public interface CRUserRepository extends CrudRepository<CRUser, String> {
}
