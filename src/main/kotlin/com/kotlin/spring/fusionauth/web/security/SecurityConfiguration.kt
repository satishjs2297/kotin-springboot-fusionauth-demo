package com.kotlin.spring.fusionauth.web.security

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfiguration(private val properties: OAuth2ResourceServerProperties) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val audiences: List<String> = properties.jwt.audiences
        val converter = CustomJwtAuthenticationConverter(audiences)
        return http.authorizeHttpRequests { authz ->
            authz
                    .requestMatchers("auth/**")
                    .hasAuthority("teller")
        }
                .oauth2ResourceServer { oauth2 ->
                    oauth2
                            .jwt { jwt ->
                                jwt.jwtAuthenticationConverter(converter)
                            }
                }
                .build()
    }

    @Bean
    fun jwtDecoder() : JwtDecoder {
        return NimbusJwtDecoder.withIssuerLocation(properties.jwt.issuerUri).build()
    }
}