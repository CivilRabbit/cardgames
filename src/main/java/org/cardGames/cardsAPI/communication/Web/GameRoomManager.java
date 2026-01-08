package org.cardGames.cardsAPI.communication.Web;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoomManager {
    private final Map<UUID, GameRoom> rooms = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> playerToRoom = new ConcurrentHashMap<>();

    public boolean addPlayerToRoom(UUID roomId, NetworkPlayer player){
        GameRoom room = rooms.get(roomId);
        if(room != null && room.canGoInRoom()){
            room.addPlayer(player);
            playerToRoom.put(player.uuid, roomId);
            return true;
        }
        return false;
    }

    public String createRoom(Games game, NetworkPlayer player){
        UUID roomId = UUID.randomUUID();
        GameRoom gameRoom = new GameRoom(game);
        rooms.put(roomId, gameRoom);
        playerToRoom.put(player.uuid, roomId);
        gameRoom.addPlayer(player);
        return roomId.toString();
    }

    public void startGame(UUID playerUUID){
        UUID roomId = playerToRoom.get(playerUUID);
        GameRoom room = rooms.get(roomId);
        room.startGame();
    }

    public void handlePlayCard(UUID playerUUID, String msg){
        UUID roomId = playerToRoom.get(playerUUID);
        GameRoom room = rooms.get(roomId);
        room.handlePlayCard(playerUUID, msg);
    }

    public List<UUID> getRooms(){
        return new ArrayList<>(rooms.keySet());
    }

}
