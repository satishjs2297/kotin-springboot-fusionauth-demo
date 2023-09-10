package com.kotlin.spring.fusionauth.web.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthCheckController {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/roles")
    fun getRoles() : List<String> {
        var roles = ArrayList<String>()
        roles.add("CLAIM_VIEW")
        roles.add("USER_CREATE")
        logger.info { "Auth Roles: $roles" }
        return roles;
    }

}