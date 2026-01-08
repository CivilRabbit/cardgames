package org.cardGames.cardsAPI.cards;

public enum Suit {
    Hearts("H"),
    Clubs("C"),
    Diamonds("D"),
    Spades("S" );

    private final String stringRepresentation;

    Suit(String stringRepresentation) {this.stringRepresentation = stringRepresentation;}
    @Override
    public String toString() { return this.stringRepresentation; }

    public static Suit fromString(String x){
        for (Suit suit: Suit.values()){
            if (suit.toString().equals(x)){
                return suit;
            }
        }
        throw new IllegalArgumentException("Value not found: " + x);
    }
}
