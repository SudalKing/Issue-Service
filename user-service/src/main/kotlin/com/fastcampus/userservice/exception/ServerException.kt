package com.fastcampus.userservice.exception

sealed class ServerException(
    val code: Int,
    override val message: String,
): RuntimeException(message)

data class UserExistsException(
    override val message: String = "이미 존재하는 사용자입니다"
): ServerException(409, message)

data class UserNotFoundException(
    override val message: String = "존재하지 않는 사용자입니다"
): ServerException(404, message)

data class PasswordNotMatchedException(
    override val message: String = "비밀번호가 틀립니다"
): ServerException(404, message)

data class InvalidTokenException(
    override val message: String = "잘못된 토큰입니다"
): ServerException(400, message)