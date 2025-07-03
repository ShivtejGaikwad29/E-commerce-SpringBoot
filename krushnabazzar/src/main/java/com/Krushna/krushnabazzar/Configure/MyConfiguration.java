package com.Krushna.krushnabazzar.Configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfiguration {

    @Autowired
    private UserDetailServiceImpl userdetailservice;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userdetailservice);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/normal/**", "/user/**", "/category/**").hasRole("USER")  // ⬅ include this line
                        .requestMatchers("/home", "/signup", "/do_register", "/profile","/order/confirm","/login","/cart","/order/place","/myorder","/addtocart/{id}","/forgot", "/send-otp", "/verify-otp", "/change-password", "/about", "/css/**", "/js/**", "/images/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/dologin")
                        .successHandler((request, response, authentication) -> {
                            authentication.getAuthorities().forEach(auth -> {
                                try {
                                    if (auth.getAuthority().equals("ROLE_ADMIN")) {
                                        response.sendRedirect("/admin/dashboard");
                                    } else if (auth.getAuthority().equals("ROLE_USER")) {
                                        response.sendRedirect("/user/index");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        })
                        .failureUrl("/faillogin")
                        .permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }

}
