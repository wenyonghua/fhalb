package org.fh.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * 说明：CSRF保护禁用
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");

		http.headers().frameOptions().disable();
    	
    	http.csrf().disable().authorizeRequests().antMatchers("/**/**").permitAll().anyRequest().authenticated().and().httpBasic();
    }
}
