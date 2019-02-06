package com.credit.reports.services;

import com.credit.reports.entities.CRUser;
import com.credit.reports.repositories.CRUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CRUserService {
    @Autowired
    private CRUserRepository CRUserRepository;

    public boolean updatePassword(String oldPassword, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        CRUser CRUser = this.CRUserRepository.findById("admin").get();
        if (passwordEncoder.matches(oldPassword, CRUser.getPassword())) {
            CRUser.setPassword(passwordEncoder.encode(newPassword));
            this.CRUserRepository.save(CRUser);
            return true;
        } else {
            return false;
        }
    }

    public String getPassword() {
        return this.CRUserRepository.findById("admin").get().getPassword();
    }
}
