package org.example.webSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Component
public class EchoHandler extends WebSocketHandler {
    List<WebSocketSession> sessions;

    public EchoHandler(List<WebSocketSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Получаем ответное сообщение от сервера
        String response = (String) message.getPayload();
        processAndSendRequestToOtherServer(response);
    }


    private void processAndSendRequestToOtherServer(String request) throws IOException {
        // Отправляем ответ обратно
        if (!sessions.isEmpty()) sessions.get(0).sendMessage(new TextMessage(request));
    }

}