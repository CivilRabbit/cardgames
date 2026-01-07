package org.cardGames.cardsAPI.player;

import org.cardGames.cardsAPI.cards.CardInterface;

import java.util.List;

public interface PlayerInterFace {
    void play();
    void reset();
    void addToHand(List<CardInterface> cards);

}
