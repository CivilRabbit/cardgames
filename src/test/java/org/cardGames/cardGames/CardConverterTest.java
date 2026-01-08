package org.cardGames.cardGames;

import org.cardGames.cardsAPI.cards.Card;
import org.cardGames.cardsAPI.cards.CardInterface;
import org.cardGames.cardsAPI.cards.Suit;
import org.cardGames.cardsAPI.cards.Value;
import org.cardGames.wippen.cards.CardConverter;
import org.cardGames.wippen.cards.MetaCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardConverterTest {
    private final Card C2 = new Card(Suit.Clubs, Value.Two);
    private final Card DA = new Card(Suit.Diamonds, Value.Ace);
    private final Card H10 = new Card(Suit.Hearts, Value.Ten);
    private final Card SJ = new Card(Suit.Spades, Value.Jack);
    private final Card HQ = new Card(Suit.Hearts, Value.Queen);
    private final List<CardInterface> one = List.of(C2, DA);
    private final MetaCard metaCard = new MetaCard(one);
    private final List<CardInterface> three = List.of(metaCard, new MetaCard(List.of(metaCard)));

    private final CardConverter converter = new CardConverter();

    @Test
    public void monoCardConverting() {
        assertEquals(List.of(C2), converter.fromString("C2"));
        assertEquals(List.of(DA), converter.fromString("DA"));
        assertEquals(List.of(H10), converter.fromString("H10"));
        assertEquals(List.of(SJ), converter.fromString("SJ"));
        assertEquals(List.of(HQ), converter.fromString("HQ"));
    }
    @Test
    public void MultipleCardConverting() {
        assertEquals(one, converter.fromString("[C2, DA]"));
        var result = List.of(new MetaCard(one));
        assertEquals(result, converter.fromString("[[C2, DA]]"));
    }

    @Test
    public void complicatedCardConverting() {
        assertEquals(three, converter.fromString("[[C2, DA], [[C2, DA]]]"));
    }

    @Test
    public void flattenTest(){
        assertEquals(List.of(C2), converter.flatten(List.of(C2)));
        assertEquals(List.of(C2, DA), converter.flatten(one));
        assertEquals(List.of(C2, DA, C2, DA), converter.flatten(three));
    }

    @Test
    public void cardValueTest(){
        assertEquals(2, converter.cardValue(C2));
        assertEquals(3, converter.cardValue(metaCard));
        assertEquals(6, converter.cardValue(new MetaCard(three)));
    }

    @Test
    public void isFaceTest(){
        assertFalse(converter.isFace(metaCard));
        assertFalse(converter.isFace(new MetaCard(List.of(HQ, SJ))));
        List<Card> nonFaces = new ArrayList<>();
        List<Card> faces = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            int index = 0;
            for (Value value : Value.values()) {
                if (index++ < 10) {
                    nonFaces.add(new Card(suit,value));
                }else{
                    faces.add(new Card(suit,value));
                }
            }
        }
        nonFaces.stream().map(converter::isFace).forEach(Assertions::assertFalse);
        faces.stream().map(converter::isFace).forEach(Assertions::assertTrue);
    }
}
