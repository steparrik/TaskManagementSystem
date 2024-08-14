package com.steparrik.utils.mapper.comment;

import com.steparrik.dto.comment.CommentResponseDto;
import com.steparrik.entity.Comment;
import com.steparrik.utils.mapper.user.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentResponseMapper {
    private final UserResponseMapper userResponseMapper;

    public CommentResponseDto toDto(Comment e) {
        if (e == null) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setText(e.getText());
        commentResponseDto.setTimestamp(e.getTimestamp());
        commentResponseDto.setId(e.getId());
        commentResponseDto.setSender(userResponseMapper.toDto(e.getSender()));
        return commentResponseDto;
    }

}
