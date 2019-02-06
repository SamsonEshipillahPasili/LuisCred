package com.credit.reports.configurations;

import com.credit.reports.entities.CRUser;
import com.credit.reports.repositories.CRUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CRUserRepository CRUserRepository;

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService((username) -> new CreditReportUserDetails(this.CRUserRepository.findById(username).get()));
    }

    public void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity)((AuthorizedUrl) ((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)
                http.authorizeRequests()
                        .antMatchers(new String[]{"/*.css", "/*.js"}))
                .permitAll()
                .antMatchers(new String[]{"/", "/search.html", "/record_history.html", "/clients.html", "/disputes.html", "/dashboard.html", "/report/pdf/*/v1", "/report/*/v1"}))
                .authenticated()
                .and())
                .formLogin()
                .loginPage("/log_in.html")
                .and()
                .authorizeRequests()
                .antMatchers(new String[]{"/report", "/report/pdf"}))
                .authenticated()
                .and())
                .httpBasic()
                .and()
                .logout()
                .logoutUrl("/log_out.html")
                .and()
                .csrf()
                .disable();
    }
}
