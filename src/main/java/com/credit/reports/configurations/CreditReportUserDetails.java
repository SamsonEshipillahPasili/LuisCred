package com.credit.reports.configurations;

import com.credit.reports.entities.CRUser;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class CreditReportUserDetails implements UserDetails {
    private CRUser CRUser;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ADMIN");
    }

    public String getPassword() {
        return this.CRUser.getPassword();
    }

    public String getUsername() {
        return this.CRUser.getUserName();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public CreditReportUserDetails(CRUser CRUser) {
        this.CRUser = CRUser;
    }
}
