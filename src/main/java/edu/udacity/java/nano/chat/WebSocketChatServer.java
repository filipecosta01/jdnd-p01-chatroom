package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSONException;
import java.io.IOException;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) {
        for(String entry : onlineSessions.keySet()) {
            final Session session = onlineSessions.get(entry);
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineSessions.put(session.getId(), session);
        final Message connectedMessage = new Message(onlineSessions.size());
        connectedMessage.setType("ENTER");
        sendMessageToAll(connectedMessage.getJSONMessageToString());

    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        try {
            final Message userMessage = new Message(jsonStr);
            sendMessageToAll(userMessage.getJSONMessageToString());
        } catch(JSONException jex) {
            System.err.println(jex.getMessage());
        }
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(session.getId(), session);
        final Message disconnectedMessage = new Message(onlineSessions.size());
        disconnectedMessage.setType("LEAVE");
        sendMessageToAll(disconnectedMessage.getJSONMessageToString());
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
