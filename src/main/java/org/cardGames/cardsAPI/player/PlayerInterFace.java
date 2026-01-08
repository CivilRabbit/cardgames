package org.cardGames.cardsAPI.player;

import org.cardGames.cardsAPI.cards.CardInterface;

import java.util.List;
import java.util.UUID;

public interface PlayerInterFace {
    String play(String msg);
    void reset();
    void addToHand(List<CardInterface> cards);
    UUID getuuid();

}
