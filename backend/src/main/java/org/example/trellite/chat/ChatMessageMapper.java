package org.example.trellite.chat;

import lombok.RequiredArgsConstructor;
import org.example.trellite.chat.dto.ChatMessageResponseDto;
import org.example.trellite.common.ObjectIdMapper;
import org.example.trellite.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ChatMessageMapper {

    private final UserRepository userRepository;
    private final ObjectIdMapper objectIdMapper;


    public ChatMessageResponseDto toResponse(ChatMessage messageModel) {
        var senderName = userRepository
                .findById(messageModel.getSenderId())
                .map( user -> user.getFirstName() + user.getLastName() )
                .orElseThrow(() -> new UsernameNotFoundException("Unknown sender."));

        return ChatMessageResponseDto.builder()
                .id(objectIdMapper.objectIdToString(messageModel.getId()))
                .boardId(messageModel.getBoardId())
                .senderId(messageModel.getSenderId())
                .senderName(senderName)
                .content(messageModel.getContent())
                .sentAt(Instant.now())
                .build();
    }

}
