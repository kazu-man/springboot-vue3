package com.group.sampleproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.group.sampleproject.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${allowed.cors.origin}")
    private String[] corsOrigin;
    @Autowired
    TokenService tokenservice;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(this.corsConfigurationSource());
        http.csrf().ignoringAntMatchers("/sample");
        // 認証
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/api/**").authenticated();
        // 独自フィルターの利用
        // デフォルトのAuthenticationManagerを利用する
        http.addFilter(new JsonAuthenticationFilter(authenticationManager(),tokenservice));
        http.addFilterAfter(new LoginFilter(tokenservice),JsonAuthenticationFilter.class);
        // csrfを無効にしておく
        // またCookieを利用してcsrf対策を行う
        http.csrf().ignoringAntMatchers("/api/**");
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addExposedHeader("X-AUTH-TOKEN");
        corsConfiguration.addAllowedOrigin(String.join(",", corsOrigin));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsSource;
    }
}