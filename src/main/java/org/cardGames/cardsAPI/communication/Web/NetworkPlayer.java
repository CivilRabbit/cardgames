package org.cardGames.cardsAPI.communication.Web;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;


public class NetworkPlayer {
    private final WebSocketSession session;
    public final UUID uuid = UUID.randomUUID();

    public NetworkPlayer(WebSocketSession session) {
        this.session = session;
    }

    public void send(GameMessage msg) {
        try {
            session.sendMessage(new TextMessage(msg.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
