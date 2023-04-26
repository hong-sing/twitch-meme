package com.ewok.twitchmeme.config;

import com.ewok.twitchmeme.domain.member.Role;
import com.ewok.twitchmeme.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).clearAuthentication(true)
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/")
                .userInfoEndpoint().userService(customOAuth2UserService);
        return http.build();
    }

}
