package faang.school.postservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEvent {
    private long postAuthorId;
    private long commentAuthorId;
    private long commentId;
    private LocalDateTime createdAt;
}
