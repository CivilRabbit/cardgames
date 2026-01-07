package org.cardGames.cardsAPI.Deck;

import org.cardGames.cardsAPI.cards.Card;
import org.cardGames.cardsAPI.cards.CardInterface;
import org.cardGames.cardsAPI.cards.Suit;
import org.cardGames.cardsAPI.cards.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DeckOfCards implements Deck {

    private final ArrayList<CardInterface> deck = new ArrayList<>();

    public List<CardInterface> getDeck(){
        return List.copyOf(deck);
    }

    public DeckOfCards() {}

    public DeckOfCards restoreToFull(){
        deck.clear();
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                deck.add(new Card(suit, value));
            }
        }
        return this;
    }

    public boolean isEmpty(){
        return deck.isEmpty();
    }

    public DeckOfCards empty(){
        deck.clear();
        return this;
    }

    public DeckOfCards shuffle() {
        Collections.shuffle(deck);
        return this;
    }

    public void removeCard(CardInterface card){
        deck.remove(card);
    }

    public void removeCards(List<CardInterface> cards){
        deck.removeAll(cards);
    }

    @Override
    public String toString() {
        return deck.toString();
    }

    public int  size(){
        return deck.size();
    }

    public void giveCards(List<? extends CardInterface> cards) {
        deck.addAll(List.copyOf(cards));
    }

    public void giveCard(CardInterface cards) {
        deck.add(cards);
    }

    public List<CardInterface> getCards(int amount) {
        if (deck.isEmpty()) {
            throw new IllegalStateException("No cards in Deck");
        }
        else {
            ArrayList<CardInterface> result = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                result.add(deck.removeFirst());
            }
            return result;
        }
    }
}
