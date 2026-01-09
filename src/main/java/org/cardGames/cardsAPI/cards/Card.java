package org.cardGames.cardsAPI.cards;

import org.jetbrains.annotations.NotNull;

public record Card(
    Suit suit,
    Value value
) implements CardInterface {

    public boolean isFace(){
        return value.isFace();
    }

    @NotNull
    @Override
    public String toString() {
        return value.toString()  + suit.toString();
    }

    public static CardInterface fromString(String s){
        return new Card(Suit.fromString(s.substring(0,1)), Value.fromString(s.substring(1)));
    }


}
