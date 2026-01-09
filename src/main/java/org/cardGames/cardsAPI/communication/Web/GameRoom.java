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
        connections.values().forEach(p -> p.send(new GameMessage(game.getCardsFrom(p.uuid), "player")));
        broadCast(new GameMessage(table, "table"));
    }

    public void broadCast(GameMessage gameMessage) {
        connections.values().forEach(player -> player.send(gameMessage));
    }

    public void handlePlayCard(UUID playerId, String input) {
        NetworkPlayer player = connections.get(playerId);
        GamePhase gm = game.getGamePhase();
        if (!gm.equals(GamePhase.Playing) && !gm.equals(GamePhase.LastRound) ) {
            player.send(new GameMessage(null, "Be patient. Game not started"));
            return;
        }
        player.send(game.play(playerId, input));
        if (game.getGamePhase() == GamePhase.Done){
            broadCast(game.countScore());
        }
        player.send(new GameMessage(game.getCardsFrom(playerId),"player"));
        broadCast(new GameMessage(table, "table"));

    }
}
