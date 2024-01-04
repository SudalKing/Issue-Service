package com.fastcampus.userservice.utils

import com.fastcampus.userservice.config.JWTProperties
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class JWTUtilsTest {

    private val logger = KotlinLogging.logger {}

    @Test
    fun `1 토큰 생성`() {
        val jwtClaim = JWTClaim(
            userId = 1,
            email = "test@test.com",
            profileUrl = "test.jpg",
            username = "Test",
        )

        val properties = JWTProperties(
            issuer = "jara",
            subject = "auth",
            expiresTime = 3600,
            secret = "my-secret"
        )

        val token = JWTUtils.createToken(jwtClaim, properties)

        assertNotNull(token)

        logger.info { "token : $token" }
    }

    @Test
    fun `2 토큰 검증`() {
        val jwtClaim = JWTClaim(
            userId = 1,
            email = "test@test.com",
            profileUrl = "test.jpg",
            username = "Test",
        )

        val properties = JWTProperties(
            issuer = "jara",
            subject = "auth",
            expiresTime = 3600,
            secret = "my-secret"
        )

        val token = JWTUtils.createToken(jwtClaim, properties)

        val decode = JWTUtils.decode(token, properties.secret, properties.issuer)

        with(decode) {
            logger.info { "claims: $claims" }

            val userId = claims["userId"]!!.asLong()
            assertEquals(userId, jwtClaim.userId)

            val email = claims["email"]!!.asString()
            assertEquals(email, jwtClaim.email)

            val profileUrl = claims["profileUrl"]!!.asString()
            assertEquals(profileUrl, jwtClaim.profileUrl)

            val username = claims["username"]!!.asString()
            assertEquals(username, jwtClaim.username)

        }
    }
}