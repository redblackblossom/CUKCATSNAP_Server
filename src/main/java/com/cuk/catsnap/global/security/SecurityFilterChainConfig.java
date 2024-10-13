package com.cuk.catsnap.global.security;

import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.global.security.filter.MemberSignInAuthenticationFilter;
import com.cuk.catsnap.global.security.filter.PhotographerSignInAuthenticationFilter;
import com.cuk.catsnap.global.security.provider.MemberAuthenticationProvider;
import com.cuk.catsnap.global.security.provider.PhotographerAuthenticationProvider;
import com.cuk.catsnap.global.security.service.MemberDetailsService;
import com.cuk.catsnap.global.security.service.PhotographerDetailsService;
import com.cuk.catsnap.global.security.util.ServletSecurityResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

    private final ObjectMapper objectMapper;
    private final ServletSecurityResponse servletSecurityResponse;
    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberDetailsService memberDetailsService() {
        return new MemberDetailsService(memberRepository);
    }

    @Bean
    public PhotographerDetailsService photographerDetailsService() {
        return new PhotographerDetailsService(photographerRepository);
    }

    @Bean
    public MemberAuthenticationProvider memberAuthenticationProvider() {
        return new MemberAuthenticationProvider(memberDetailsService(), passwordEncoder());
    }

    @Bean
    public PhotographerAuthenticationProvider photographerAuthenticationProvider() {
        return new PhotographerAuthenticationProvider(photographerDetailsService(), passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(memberAuthenticationProvider());
        providers.add(photographerAuthenticationProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public MemberSignInAuthenticationFilter memberSignInAuthenticationFilter() throws Exception {
        return new MemberSignInAuthenticationFilter(authenticationManager(), objectMapper, servletSecurityResponse);
    }

    @Bean
    public PhotographerSignInAuthenticationFilter photographerSignInAuthenticationFilter() throws Exception {
        return new PhotographerSignInAuthenticationFilter(authenticationManager(), objectMapper, servletSecurityResponse);
    }



    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
            .formLogin(FormLoginConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .addFilterAt(memberSignInAuthenticationFilter(), BasicAuthenticationFilter.class)
            .addFilterAt(photographerSignInAuthenticationFilter(), BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests->
                    authorizeRequests
                    .anyRequest().permitAll()
            );
        return http.build();
    }
}
