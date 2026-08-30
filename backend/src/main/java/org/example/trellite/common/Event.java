package org.example.trellite.common;

public record Event(
        String boardId,
        EventType action
) { }
