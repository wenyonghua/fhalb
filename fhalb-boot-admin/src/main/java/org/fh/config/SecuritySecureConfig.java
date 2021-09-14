package org.fh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

/**
 * 说明：SecuritySecure配置
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

	private final String adminContextPath;

	public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
		this.adminContextPath = adminServerProperties.getContextPath();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");

		http.headers().frameOptions().disable();
		
		http.authorizeRequests().antMatchers(adminContextPath + "/assets/**",adminContextPath + "/actuator/**").permitAll()
				.antMatchers(adminContextPath + "/login").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage(adminContextPath + "/login").successHandler(successHandler).and().logout()
				.logoutUrl(adminContextPath + "/logout").and().httpBasic().and().csrf().disable();

	}
	
}
