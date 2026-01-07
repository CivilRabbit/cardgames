package org.cardGames.wippen;

import org.cardGames.cardsAPI.Deck.DeckOfCards;
import org.cardGames.cardsAPI.communication.Communicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Wippen {

    private final DeckOfCards drawDeck = new DeckOfCards();
    private final DeckOfCards onTable = new DeckOfCards();
    private final List<WipPlayer> players = new ArrayList<>();

    public Wippen(int amountOfPlayers, Communicator communicator) {
        drawDeck.restoreToFull().shuffle();
        onTable.empty();
        for (int i = 0; i < amountOfPlayers; i++) {
            players.add(new WipPlayer("Player" + i, onTable, communicator));
        }
    }

    private void deal(boolean firstRound){
        int counter ;
        int length = players.size();
        for (int i = 0; i < 2; i++) {
            counter = 0;
            for (WipPlayer player : players) {
                if (firstRound && (counter++) + 1 == length) {
                    onTable.giveCards(drawDeck.getCards(2));
                }
                int cardsToDeal = drawDeck.size() > 1 ? 2 : drawDeck.size();
                player.addToHand(drawDeck.getCards(cardsToDeal));
            }
            if  (drawDeck.isEmpty()){
                break;
            }
        }
    }

    public void startGame(){
        players.forEach(WipPlayer::reset);
        deal(true);
        boolean lastRound = false;
        WipPlayer lastPlayer = null;
        while (!drawDeck.isEmpty() || lastRound){
            for (int i = 0; i < 4; i++) {
                int tableSize = onTable.size();
                for (WipPlayer player : players) {
                    player.play();
                    if (!drawDeck.isEmpty() && onTable.isEmpty()){
                        player.giveWip();
                    }
                    // what is the last player to take a card
                    if (onTable.size() < tableSize){lastPlayer = player;}
                }
            }
            if (lastRound){break;}
            deal(false);
            lastRound = drawDeck.isEmpty();
        }
        assert lastPlayer != null;
        lastPlayer.giveCards(onTable.getDeck());
        countScore().forEach((player, score) -> System.out.println(player.name + ": " + score.toString()));
    }

    private Map<WipPlayer, Integer> countScore(){
        Map<WipPlayer, Integer> scoreBoard = new HashMap<>();

        List<Score> scoreList = new ArrayList<>();
        List<WipPlayer> playerList = new ArrayList<>();

        for (WipPlayer player : players) {
            Score pScore  = player.calculateScore();
            int iscore = pScore.aceCount() + pScore.wips();
            if (pScore.has2ofClubs()) iscore += 1;
            if (pScore.has10OfDiamonds()) iscore += 2;
            playerList.add(player);
            scoreList.add(pScore);
            scoreBoard.put(player, iscore);
        }

        WipPlayer mostCard = playerList.get(Score.mostCards(scoreList));
        scoreBoard.put(mostCard, scoreBoard.get(mostCard) + 2);

        WipPlayer mostClubs = playerList.get(Score.mostClubs(scoreList));
        scoreBoard.put(mostClubs, scoreBoard.get(mostClubs) + 2);
        return scoreBoard;
    }
}
