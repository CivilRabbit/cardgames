package org.cardGames.cardsAPI.communication.Web;


import org.cardGames.cardsAPI.Deck.Deck;
import org.jetbrains.annotations.NotNull;

public record GameMessage(Deck cards, String message){

    @NotNull
    @Override
    public String toString(){
        return "message="+message+", cards="+(cards == null ? "" : cards.toString());
    }

}
