package com.thxbrop.springboot.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/user/{userId}")
public class UserWebsocket {
    private static final CopyOnWriteArraySet<UserWebsocket> userWebsockets = new CopyOnWriteArraySet<>();
    private static final Map<Integer, Session> sessionPool = new HashMap<>();
    private Session session;
    private OnEventListener listener;


    public void setListener(OnEventListener listener) {
        this.listener = listener;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId) {
        this.session = session;
        userWebsockets.add(this);
        sessionPool.put(userId, session);
        Optional.ofNullable(listener).ifPresent(OnEventListener::onConnect);
    }

    @OnClose
    public void onClose() {
        userWebsockets.remove(this);
        Optional.ofNullable(listener).ifPresent(OnEventListener::onDisconnect);
    }

    @OnMessage
    public void onMessage(String message) {
        Optional.ofNullable(listener).ifPresent(onEventListener -> onEventListener.onMessage(message));
    }

    public void sendText(String message, int targetUserId) {
        Optional.ofNullable(sessionPool.get(targetUserId)).ifPresent(s -> s.getAsyncRemote().sendText(message));
    }

    public void sendText(String message, int[] targetUserIds) {
        for (int id : targetUserIds) {
            Optional.ofNullable(sessionPool.get(id)).ifPresent(s -> s.getAsyncRemote().sendText(message));
        }
    }
}
