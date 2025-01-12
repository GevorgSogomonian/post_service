package faang.school.postservice.validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.user.UserDto;
import faang.school.postservice.exception.OperationNotAvailableException;
import faang.school.postservice.model.Comment;
import faang.school.postservice.repository.CommentRepository;
import faang.school.postservice.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentValidatorTest {
    @InjectMocks
    private CommentValidator commentValidator;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    private long userId;
    private long postId;
    private long commentId;
    private Comment comment;
    private UserDto userDto;

    @BeforeEach
    void init() {
        userId = 1L;
        postId = 1L;
        commentId = 2L;
        comment = Comment.builder()
                .id(commentId)
                .authorId(1L).build();
        userDto = UserDto.builder().id(userId)
                .username("user")
                .email("some@mail.ru")
                .build();
    }

    @Test
    void existUserTest() {
        long userId = 1L;
        when(userServiceClient.getUser(anyLong())).thenReturn(userDto);
        commentValidator.existUser(userId);
        verify(userServiceClient).getUser(userId);
    }

    @Test
    void existPostTest() {
        when(postRepository.existsById(postId)).thenReturn(true);
        commentValidator.existPost(postId);
        verify(postRepository).existsById(postId);
    }

    @Test
    void existPostThrowExceptionTest() {
        when(postRepository.existsById(postId)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> commentValidator.existPost(postId));
    }

    @Test
    void existCommentTest() {
        when(commentRepository.existsById(commentId)).thenReturn(true);
        commentValidator.existComment(commentId);
        verify(commentRepository).existsById(commentId);
    }

    @Test
    void existCommentThrowExceptionTest() {
        when(commentRepository.existsById(commentId)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> commentValidator.existComment(commentId));
    }

    @Test
    void checkUserIsAuthorCommentTest() {
        commentValidator.checkUserIsAuthorComment(comment, userId);
    }

    @Test
    void checkUserIsAuthorCommentTestThrowException() {
        assertThrows(OperationNotAvailableException.class, () -> commentValidator.checkUserIsAuthorComment(comment, 99l));
    }
}
