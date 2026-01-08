package org.cardGames.cardsAPI.communication.Web;

import org.cardGames.cardsAPI.Deck.DeckOfCards;
import org.cardGames.cardsAPI.game.CardGame;
import org.cardGames.wippen.Wippen;

import java.lang.reflect.InvocationTargetException;

public enum Games {
    WIPPEN(Wippen.class);

    private final Class<? extends CardGame> game;
    Games(Class<? extends CardGame> game){
        this.game = game;
    }

    public CardGame create(DeckOfCards table){
        try{
            return game.getDeclaredConstructor(DeckOfCards.class).newInstance(table);
        }catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
}
