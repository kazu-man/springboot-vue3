package com.group.sampleproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.group.sampleproject.service.TokenService;
import com.group.sampleproject.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${allowed.cors.origin}")
    private String[] corsOrigin;
    @Autowired
    TokenService tokenservice;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailService;
    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        //cors設定を反映
        http.cors().configurationSource(this.corsConfigurationSource());

        // 認証
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/signUp").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/api/**").authenticated();
        
        //jwtでの認証フィルターをセット
        http.addFilterAfter(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);

        // csrfを無効
        http.csrf().ignoringAntMatchers("/api/**");

        // 独自フィルターの利用する場合
        // デフォルトのAuthenticationManagerを利用する
        // http.addFilter(new JsonAuthenticationFilter(authenticationManager(),tokenservice,userService));
        // http.addFilterAfter(JwtAuthFilter,JsonAuthenticationFilter.class);
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

    @Bean
    public AuthenticationManager authenticatÏionManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        //テスト用DBのパスワードがエンコードされてないのでテスト用に必ず認証できるエンコーダ
        // provider.setPasswordEncoder(new TestBCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}