package com.jellybrains.quietspace.common_service.constant;

public class ChatPathValues {
    public static final String CHAT_PATH = "/api/v1/chats";
    public static final String SOCKET_CHAT_PATH = "/private/chat";
    public static final String CHAT_EVENT_PATH = SOCKET_CHAT_PATH + "/event";
    public static final String LEAVE_CHAT_PATH = SOCKET_CHAT_PATH + "/leave";
    public static final String JOIN_CHAT_PATH = SOCKET_CHAT_PATH + "/join";
    public static final String DELETE_MESSAGE_PATH = SOCKET_CHAT_PATH + "/delete/{messageId}";
    public static final String SEEN_MESSAGE_PATH = SOCKET_CHAT_PATH + "/seen/{messageId}";
    public static final String PUBLIC_CHAT_PATH = "/public/chat";
}
