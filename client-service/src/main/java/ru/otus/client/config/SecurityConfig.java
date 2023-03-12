package ru.otus.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AppConfig appConfig;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository, AppConfig appConfig) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.appConfig = appConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers("/login", "/logout", "/error").permitAll();
                authorize.anyRequest().authenticated();
            })
            .logout(logout -> {
                logout.logoutSuccessHandler(oidcLogoutSuccessHandler())
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("http://localhost:8080/login?logout")
//                    .deleteCookies("JSESSIONID")
                ;
            });

        http
            .oauth2Login()
            .loginPage(appConfig.getLoginPage());

        return http.build();
    }

    // в spring oauth2-authorization-server пока не реализовано
    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
//        oidcLogoutSuccessHandler.setPostLogoutRedirectUri();
        return oidcLogoutSuccessHandler;
    }

}
