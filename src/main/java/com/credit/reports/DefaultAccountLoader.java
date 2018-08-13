package com.credit.reports;

import com.credit.reports.entities.CRUser;
import com.credit.reports.repositories.CRUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccountLoader implements CommandLineRunner {
    @Autowired
    private CRUserRepository crUserRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public DefaultAccountLoader() {
    }

    public void run(String... args) throws Exception {
        CRUser CRUser;
        if (!this.crUserRepository.existsById("admin")) {
            CRUser = new CRUser();
            CRUser.setUserName("admin");
            CRUser.setPassword(this.passwordEncoder.encode("password"));
            this.crUserRepository.save(CRUser);
        }

        if (!this.crUserRepository.existsById("flyingSaucer")) {
            CRUser = new CRUser();
            CRUser.setUserName("flyingSaucer");
            CRUser.setPassword(this.passwordEncoder.encode("flyingSaucerPassword"));
            this.crUserRepository.save(CRUser);
        }

    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
