package org.cardGames.cardsAPI.cards;

public enum Value {
    Ace("A"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Jack("J"),
    Queen("Q"),
    King("K");

    public boolean isFace() {
        return this == Jack || this == Queen || this == King;
    }

    private final String stringRepresentation;
    Value(String stringRepresentation) {this.stringRepresentation = stringRepresentation;}

    @Override
    public String toString() {
        return this.stringRepresentation;
    }

    public static Value fromString(String x){
        for (Value value: Value.values()){
            if (value.toString().equals(x)){
                return value;
            }
        }
        throw new IllegalArgumentException("Value not found: " + x);
    }
}
