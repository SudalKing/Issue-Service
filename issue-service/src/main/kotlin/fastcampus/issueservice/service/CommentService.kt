package fastcampus.issueservice.service

import fastcampus.issueservice.domain.Comment
import fastcampus.issueservice.domain.CommentRepository
import fastcampus.issueservice.domain.IssueRepository
import fastcampus.issueservice.exception.NotFoundException
import fastcampus.issueservice.model.CommentRequest
import fastcampus.issueservice.model.CommentResponse
import fastcampus.issueservice.model.toResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val issueRepository: IssueRepository,
) {

    @Transactional
    fun create(issueId: Long, userId: Long, username: String, request: CommentRequest): CommentResponse {
        val issue = issueRepository.findByIdOrNull(issueId)?: throw NotFoundException("이슈가 존재하지 않습니다")

        val comment = Comment(
            issue = issue,
            userId = userId,
            username = username,
            body = request.body,
        )

        issue.comments.add(comment)                         // insert query 까지 동작하지만
        return commentRepository.save(comment).toResponse() // 명시적 사용 - (방법2)
    }

    @Transactional
    fun edit(commentId: Long, userId: Long, request: CommentRequest): CommentResponse? {
        return commentRepository.findByIdAndUserId(commentId, userId)?.run {
            body = request.body
            commentRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun delete(issueId: Long, commentId: Long, userId: Long) {
        val issue = issueRepository.findByIdOrNull(issueId)?: throw NotFoundException("이슈가 존재하지 않습니다")

        commentRepository.findByIdAndUserId(commentId, userId)?.let {
            comment -> issue.comments.remove(comment)
        }
    }

}