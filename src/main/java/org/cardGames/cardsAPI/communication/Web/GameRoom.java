package org.cardGames.cardsAPI.communication.Web;

import org.cardGames.cardsAPI.Deck.DeckOfCards;
import org.cardGames.cardsAPI.game.CardGame;
import org.cardGames.cardsAPI.game.GamePhase;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {
    private final Map<UUID, NetworkPlayer> connections = new ConcurrentHashMap<>();
    private final CardGame game;
    private final DeckOfCards table = new DeckOfCards();
    public final Games type;

    public GameRoom(Games game){
        this.type = game;
        this.game = game.create(this.table);
    }

    public boolean canGoInRoom(){
        return connections.size() < game.maxPlayers() && game.getGamePhase() == GamePhase.NotStarted;
    }

    public void addPlayer(NetworkPlayer player) {
        UUID uuid  = player.uuid;
        connections.put(uuid, player);
        game.addPlayer(uuid);
    }

    public void startGame() {
        game.startGame();
        broadCast(new GameMessage(table, "current table"));
        sendTurnOf();
    }

    private void sendTurnOf(){
        UUID turn = UUID.fromString(game.TurnOF().message());
        sendTo(connections.get(turn), new GameMessage(game.getCardsFrom(turn),"It is your turn. Your cards are:"));
    }

    public void broadCast(GameMessage gameMessage) {
        connections.values().forEach(player -> player.send(gameMessage));
    }

    public void handlePlayCard(UUID playerId, String input) {
        sendTo(connections.get(playerId), game.play(playerId, input));
        if (game.getGamePhase() == GamePhase.Done){
            broadCast(game.countScore());
        }
        broadCast(new GameMessage(table, "Current table"));
        sendTurnOf();
    }

    public void sendTo(NetworkPlayer player, GameMessage message) {
        player.send(message);
    }
}
