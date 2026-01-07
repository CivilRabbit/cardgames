package org.cardGames.wippen;

import java.util.List;

public record Score(
        int cardCount,
        int clubCount,
        int aceCount,
        int wips,
        boolean has10OfDiamonds,
        boolean has2ofClubs
) {
    public static int mostCards(List<Score> scores){
        int index = 0;
        int max = 0;
        for (int i = 0; i< scores.size();i++){
            int cardCount = scores.get(i).cardCount();
            if (cardCount > max) {
                max = cardCount;
                index = i;
            }
        }
        return index;
    }

    public static int mostClubs(List<Score> scores){
        int index = 0;
        int max = 0;
        for (int i = 0; i< scores.size();i++){
            int cardCount = scores.get(i).clubCount();
            if (cardCount > max) {
                max = cardCount;
                index = i;
            }
        }
        return index;
    }
}
