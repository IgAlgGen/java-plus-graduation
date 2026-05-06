package ewm.comment.mapper;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.NewCommentDto;
import ewm.comment.dto.UpdateCommentRequest;
import ewm.comment.model.Comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment mapToComment(NewCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        return comment;
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(getAuthorId(comment))
                .event(getEventId(comment))
                .status(comment.getStatus().name())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .build();
    }

    public static Comment updateComment(Comment comment, UpdateCommentRequest updateCommentRequest) {
        if (updateCommentRequest.hasText()) {
            comment.setText(updateCommentRequest.getText());
        }
        return comment;
    }

    public static Long getEventId(Comment comment) {
        if (comment.getEventId() != null) {
            return comment.getEventId();
        }
        return comment.getEvent().getId();
    }

    public static Long getAuthorId(Comment comment) {
        if (comment.getAuthorId() != null) {
            return comment.getAuthorId();
        }
        return comment.getAuthor().getUserId();
    }
}
