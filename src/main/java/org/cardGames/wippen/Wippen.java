package org.cardGames.wippen;

import lombok.Getter;
import org.cardGames.cardsAPI.communication.Web.Games;
import org.cardGames.cardsAPI.game.CardGame;
import org.cardGames.cardsAPI.Deck.DeckOfCards;
import org.cardGames.cardsAPI.communication.Web.GameMessage;
import org.cardGames.cardsAPI.game.GamePhase;

import java.util.*;


public class Wippen implements CardGame {


    private final DeckOfCards drawDeck = new DeckOfCards();
    private final DeckOfCards onTable;
    private final List<WipPlayer> players = new ArrayList<>();
    private UUID turn;
    @Getter
    private final Games name = Games.WIPPEN;

    @Getter
    private GamePhase gamePhase = GamePhase.NotStarted;
    private WipPlayer lastToTake = null;
    private int cardsInHand = 0;

    public Wippen(DeckOfCards table) {
        drawDeck.restoreToFull().shuffle();
        onTable = table.empty();
    }

    public GameMessage TurnOF() { return new GameMessage(null, turn.toString()); }

    public DeckOfCards getCardsFrom(UUID playerId) {
        WipPlayer player = getWipPlayer(playerId);
        assert player != null;
        return player.getHandCards();
    }

    public boolean addPlayer(UUID player) {
        if (players.size() < 4 && getWipPlayer(player) == null) {
            players.add(new WipPlayer(onTable, player));
            return true;
        }
        return false;
    }

    public int maxPlayers(){return 4;}


    public void startGame() {
        players.forEach(WipPlayer::reset);
        onTable.empty();
        deal(true);
        turn = players.getFirst().getuuid();
        gamePhase = GamePhase.Playing;
    }

    @Override
    public GameMessage play(UUID playerId, String input) {
        if (turn != playerId){
            return new GameMessage(onTable, "Await You Turn");
        }
        WipPlayer player = getWipPlayer(playerId);
        assert player != null;

        int tableSizeBefore = onTable.size();
        String result = player.play(input);
        if (!result.equals("success") && !result.equals("merge")){
            return new GameMessage(onTable, result);
        }
        if (onTable.size() < tableSizeBefore) {
            lastToTake = player;
        }
        if (onTable.isEmpty() && gamePhase == GamePhase.Playing){
            player.giveWip();
        }

        if (!result.equals("merge") && gamePhase == GamePhase.Playing || gamePhase == GamePhase.LastRound){
            advance();
        }
        return new GameMessage(onTable, result);
    }


    public GameMessage countScore(){
        if (lastToTake != null){lastToTake.giveCards(onTable.getDeck());}
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
        return new GameMessage(null, scoreBoard.toString());
    }

    private void advance(){
        int nextTurn = Math.floorMod(players.indexOf(getWipPlayer(turn)) + 1,  players.size());
        if (nextTurn == 0 && gamePhase == GamePhase.LastRound){
            gamePhase = GamePhase.Done;
            return;
        }else if (nextTurn == 0){
            if (--cardsInHand == 0) deal(false);
            turn = players.getFirst().getuuid();
        }
        turn = players.get(nextTurn).getuuid();
    }

    private void deal(boolean firstRound){
        cardsInHand = 4;
        int counter ;
        int length = players.size();
        for (int i = 0; i < 2; i++) {
            counter = 0;
            for (WipPlayer player : players) {
                if (firstRound && (counter++) + 1 == length) {
                    onTable.giveCards(drawDeck.getCards(2));
                }
                player.addToHand(drawDeck.getCards(2));
            }
            if  (drawDeck.isEmpty()){
                gamePhase = GamePhase.LastRound;
                break;
            }
        }
    }

    private WipPlayer getWipPlayer(UUID playerId){
        for (WipPlayer player : players) {
            if (player.getuuid() .equals(playerId)){
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Wippen);
    }

    @Override
    public int hashCode() {
        return Objects.hash("Wippen");
    }
}