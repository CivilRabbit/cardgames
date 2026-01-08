package org.cardGames.cardsAPI.game;


import org.cardGames.cardsAPI.Deck.DeckOfCards;
import org.cardGames.cardsAPI.communication.Web.GameMessage;

import java.util.UUID;

public interface CardGame {
    int maxPlayers();
    GameMessage play(UUID playerId, String something);
    GamePhase getGamePhase();
    GameMessage countScore();
    void startGame();
    GameMessage TurnOF();
    DeckOfCards getCardsFrom(UUID playerId);
    boolean addPlayer(UUID playerId);

}
