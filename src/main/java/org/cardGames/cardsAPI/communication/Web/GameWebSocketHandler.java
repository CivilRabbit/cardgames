package org.cardGames.cardsAPI.communication.Web;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, NetworkPlayer> players = new ConcurrentHashMap<>();
    private final GameRoomManager gameRoomManager = new GameRoomManager();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        players.put(session.getId(), new NetworkPlayer(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String msg = message.getPayload();
        System.out.println("GAME MESSAGE: " + msg);
        String[] split = msg.split(" ");
        String returnMessage=null;
        NetworkPlayer player = players.get(session.getId());
        if (split[0].contains("join")) {
            returnMessage = gameRoomManager.addPlayerToRoom(UUID.fromString(split[1]), player) ? "joined" : "failed to join";

        }
        else if (split[0].contains("create"))  {
            returnMessage = gameRoomManager.createRoom(Games.valueOf(split[1].toUpperCase()), player);
        }
        else if (split[0].contains("start")) gameRoomManager.startGame(player.uuid);
        else if (split[0].contains("rooms")) returnMessage = "available rooms" + gameRoomManager.getRooms().toString();
        else gameRoomManager.handlePlayCard(player.uuid, msg);
        if (returnMessage != null) player.send(new GameMessage(null, returnMessage));
    }
}
