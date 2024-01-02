package fastcampus.issueservice.model

import fastcampus.issueservice.domain.Comment


data class CommentRequest(
    val body: String,
)

data class CommentResponse(
    val id: Long,
    val issueId: Long,
    val userId: Long,
    val body: String,
    val username: String? = null,
)

// 방법1. companion object 안에 invoke 생성

// 방법2. kotlin 확장 함수 사용
fun Comment.toResponse() =
    CommentResponse(
        id = id!!,
        issueId = issue.id!!,
        userId = userId,
        body = body,
        username = username,
    )