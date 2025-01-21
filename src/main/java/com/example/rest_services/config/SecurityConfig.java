package com.example.rest_services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.Saml2X509Credential;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class SecurityConfig {

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() throws Exception {
        // Load the IdP certificate from the pem file
        ClassPathResource resource = new ClassPathResource("idp-certificate.pem");
        try (InputStream inputStream = resource.getInputStream()) { // Try-with-resources for proper closing
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate idpCertificate = (X509Certificate) factory.generateCertificate(inputStream);

            RelyingPartyRegistration registration = RelyingPartyRegistration
                    .withRegistrationId("miniOrange")
                    .entityId("http://localhost:8080/saml2/service-provider-metadata/mini0range")
                    .assertionConsumerServiceLocation("http://localhost:8080/login/saml2/sso/mini0range")
                    .idpEntityId("https://idp.mini0range.com")
                    .idpCertificates(Saml2X509Credential.x509(idpCertificate)) // Add the certificate
                    .build();

            return new InMemoryRelyingPartyRegistrationRepository(registration);
        }
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .saml2Login(Saml2LoginConfigurer::relyingPartyRegistrationRepository);
        return http.build();
    }
}
