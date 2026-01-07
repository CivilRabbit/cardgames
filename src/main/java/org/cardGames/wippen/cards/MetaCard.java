package org.cardGames.wippen.cards;

import org.cardGames.cardsAPI.cards.CardInterface;

import java.util.Arrays;
import java.util.List;

public class MetaCard implements CardInterface {
    public List<CardInterface> cards;

    public MetaCard(List<CardInterface> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    @Override
    public boolean equals(Object x) {
        return x instanceof MetaCard mc && Arrays.equals(cards.toArray(),mc.cards.toArray());
    }

    @Override
    public int hashCode() {
        return cards.hashCode();
    }
}
