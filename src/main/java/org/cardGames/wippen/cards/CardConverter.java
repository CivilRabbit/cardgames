package org.cardGames.wippen.cards;

import org.cardGames.cardsAPI.cards.Card;
import org.cardGames.cardsAPI.cards.CardInterface;
import org.cardGames.cardsAPI.cards.Value;

import java.util.ArrayList;
import java.util.List;

public class CardConverter {
    public List<CardInterface> fromString(String cards) {
        return fromString(cards, cards.startsWith("["));
    }

    public List<CardInterface> fromString(String cards, boolean start) {
        List<CardInterface> cardList = new ArrayList<>();
        int index = start ? 1 : 0;
        while (index < cards.length()) {
            if (cards.charAt(index) == '[') {
                MetaCard metaCard = new MetaCard(fromString(cards.substring(++index), false));
                cardList.add(metaCard);
                while (cards.charAt(++index) != ']') {}
                index++;
            } else if (cards.charAt(index) == ']') {
                break;
            } else if (List.of(',', ' ').contains(cards.charAt(index))) {
                index++;
            } else {
                int newIndex = index;
                while (newIndex < cards.length() && cards.charAt(newIndex) != ']' && cards.charAt(newIndex) != ',') {
                    newIndex++;
                }
                cardList.add(Card.fromString(cards.substring(index, newIndex)));
                index = newIndex;
            }
        }
        return cardList;
    }

    public List<Card> flatten(List<CardInterface> cards) {
        List<Card> flattened = new ArrayList<>();
        for (CardInterface card : cards) {
            switch (card) {
                case Card c -> flattened.add(c);
                case MetaCard mc -> flattened.addAll(flatten(mc.cards));
                default -> throw new IllegalArgumentException("Not an existent type of card");
            }
        }
        return flattened;
    }

    public int cardValue(CardInterface card) {
        if (card instanceof MetaCard mc) {
            return mc.cards.stream().map(this::cardValue).reduce(0, Integer::sum);
        }
        if (card instanceof Card c) {
            Value[] values = Value.values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].equals(c.value())) {
                    return i+1;
                }
            }
        }
        throw new IllegalArgumentException("Not an existent type of card");
    }

    public boolean isFace(CardInterface card) {
        if (card instanceof MetaCard) {
            return false;
        }else if (card instanceof Card c) {
            return c.isFace();
        }
        throw new IllegalArgumentException("Not an existent type of card");
    }
}
