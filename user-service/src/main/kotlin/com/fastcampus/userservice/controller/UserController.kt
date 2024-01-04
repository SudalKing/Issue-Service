package com.fastcampus.userservice.controller

import com.fastcampus.userservice.domain.service.UserService
import com.fastcampus.userservice.model.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest) =
        userService.signUp(signUpRequest)

    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest): SignInResponse =
        userService.signIn(signInRequest)

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun logout(@AuthToken token: String) =
        userService.logout(token)

    /**
     * @PathVariable 을 통해 {userId} 를 받는 것은 보안취약점이 될 수 있으므로
     * token 을 받아 응답 객체를 wrapping 해 반환
     */
    @GetMapping("/me")
    suspend fun get(@AuthToken token: String): MeResponse =
        MeResponse(userService.getByToken(token))

    /**
     * {userId} 를 받는다면 한정적인 정보만 제공
     */
    @GetMapping("/{userId}/username")
    suspend fun getUsername(@PathVariable userId: Long): Map<String, String> {
        return mapOf("reporter" to userService.get(userId).username)
    }

    @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id: Long,
        @ModelAttribute request: UserEditRequest,
        @AuthToken token: String,
        @RequestPart("profileUrl") filePart: FilePart,
    ) {
        val orgFilename = filePart.filename()
        var filename: String? = null

        if (orgFilename.isNotEmpty()) {
            val ext = orgFilename.substring(orgFilename.lastIndexOf(".") + 1)
            filename = "${id}.${ext}"

            // resource/images/1.jpg
            val file = File(ClassPathResource("/images/").file, filename)
            filePart.transferTo(file).awaitSingleOrNull()
        }

        userService.edit(token, request.username, filename)
    }
}