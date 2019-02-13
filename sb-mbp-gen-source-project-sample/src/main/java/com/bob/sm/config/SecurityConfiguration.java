package com.bob.sm.config;

import com.bob.sm.security.jwt.JWTConfigurer;
import com.bob.sm.security.jwt.TokenProvider;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
                                 UserDetailsService userDetailsService,
                                 TokenProvider tokenProvider,
                                 CorsFilter corsFilter,
                                 SecurityProblemSupport problemSupport) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            // 这里写放开权限的URL
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/static/**")
            .antMatchers("/swagger-ui.html");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            // ==================需要关闭的Rest接口写在这里 start======================
            .antMatchers(HttpMethod.POST, "/api/sample-urls").denyAll()
            // ==================需要关闭的Rest接口写在这里 end======================

            // ==================需要放开认证的Rest接口写在这里 start======================
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/get-open-id").permitAll()
            // ==================需要放开认证的Rest接口写在这里 end======================

            // ==================需要认证的接口范围写在这里 start======================
            .antMatchers("/api/**").authenticated()
            // ==================需要认证的接口范围写在这里 end======================

            // ==================系统健康检查 start======================
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/**").hasAuthority(Constants.ROLE_ADMIN)
            // ==================系统健康检查 end======================
            .and()
            .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
