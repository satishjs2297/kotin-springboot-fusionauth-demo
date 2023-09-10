package com.kotlin.spring.fusionauth.web.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class CustomJwtAuthenticationConverter(private val audiences: List<String>) : Converter<Jwt, AbstractAuthenticationToken> {
    companion object {
        private const val ROLES_CLAIM = "roles"
        private const val EMAIL_CLAIM = "email"
        private const val AUD_CLAIM = "aud"
    }

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val email = jwt.getClaimAsString(EMAIL_CLAIM)
        return if (!hasAudience(jwt)) {
            UsernamePasswordAuthenticationToken(email, "n/a")
        } else {
            val authorities = extractRoles(jwt)
            UsernamePasswordAuthenticationToken(email, "n/a", authorities)
        }
    }

    private fun hasAudience(jwt: Jwt): Boolean {
        return jwt.hasClaim(AUD_CLAIM)
                && jwt.getClaimAsStringList(AUD_CLAIM).any { audiences.contains(it) }
    }

    private fun extractRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val roles = jwt.getClaimAsStringList(ROLES_CLAIM)
        return roles.map { SimpleGrantedAuthority(it) }
    }
}