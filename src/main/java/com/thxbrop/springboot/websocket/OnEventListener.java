package com.thxbrop.springboot.websocket;

public interface OnEventListener {
    void onMessage(String message);

    void onConnect();

    void onDisconnect();
}
