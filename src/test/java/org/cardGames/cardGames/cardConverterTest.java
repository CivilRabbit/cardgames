package org.cardGames.cardGames;

import org.cardGames.cardsAPI.cards.Card;
import org.cardGames.cardsAPI.cards.CardInterface;
import org.cardGames.cardsAPI.cards.Suit;
import org.cardGames.cardsAPI.cards.Value;
import org.cardGames.wippen.cards.CardConverter;
import org.cardGames.wippen.cards.MetaCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class cardConverterTest {
    private final CardConverter converter = new CardConverter();

    @Test
    public void monoCardConverting() {
        assertEquals(List.of(new Card(Suit.Clubs, Value.Two)), converter.fromString("C2"));
        assertEquals(List.of(new Card(Suit.Diamonds, Value.Ace)), converter.fromString("DA"));
        assertEquals(List.of(new Card(Suit.Hearts, Value.Ten)), converter.fromString("H10"));
        assertEquals(List.of(new Card(Suit.Spades, Value.Jack)), converter.fromString("SJ"));
        assertEquals(List.of(new Card(Suit.Hearts, Value.Queen)), converter.fromString("HQ"));
    }
    @Test
    public void MultipleCardConverting() {
        List<CardInterface> result = List.of(new Card(Suit.Clubs, Value.Two), new Card(Suit.Clubs, Value.Ace));
        assertEquals(result, converter.fromString("[C2, CA]"));
        result = List.of(new MetaCard(result));
        assertEquals(result, converter.fromString("[[C2, CA]]"));
    }

    @Test
    public void complicatedCardConverting() {
        List<CardInterface> one = List.of(new Card(Suit.Clubs, Value.Two), new Card(Suit.Clubs, Value.Ace));
        var two = new MetaCard(one);
        var three = List.of(two, new MetaCard(List.of(two)));
        assertEquals(three, converter.fromString("[[C2, CA], [[C2, CA]]]"));
    }
}
