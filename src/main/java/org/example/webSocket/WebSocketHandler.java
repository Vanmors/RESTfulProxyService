package org.example.webSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketClient client = new StandardWebSocketClient();
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    @Override

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        sessions.add(session);
        WebSocketSession echoSession = client.execute(new EchoHandler(sessions), "wss://echo.websocket.org").get();
        echoSession.sendMessage(message);

    }
}
