package org.cardGames.wippen;

import org.cardGames.cardsAPI.Deck.DeckOfCards;
import org.cardGames.cardsAPI.cards.Card;
import org.cardGames.cardsAPI.cards.CardInterface;
import org.cardGames.cardsAPI.cards.Suit;
import org.cardGames.cardsAPI.cards.Value;
import org.cardGames.cardsAPI.communication.Communicator;
import org.cardGames.cardsAPI.player.PlayerInterFace;
import org.cardGames.wippen.cards.CardConverter;
import org.cardGames.wippen.cards.MetaCard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WipPlayer implements PlayerInterFace {
    private final DeckOfCards handCards = new DeckOfCards();
    private final List<Card> cardsWon = new ArrayList<>();
    private final CardConverter cardConverter = new CardConverter();
    private final DeckOfCards table;
    private int wips = 0;
    private CardInterface build;
    private final ArrayList<CardInterface> metaCards = new ArrayList<>();
    public final String name;
    private final Communicator communicator;

    public WipPlayer(String name, DeckOfCards table, Communicator communicator) {
        this.table = table;
        this.name = name;
        this.communicator = communicator;
    }

    public void reset(){
        handCards.empty();
        cardsWon.clear();
        wips = 0;
        build = null;
        metaCards.clear();
    }

    public void addToHand(List<CardInterface> cards){
        this.handCards.giveCards(List.copyOf(cards));
    }

    public void giveCards(List<CardInterface> cards){
        this.cardsWon.addAll(cardConverter.flatten(cards));
    }

    public void giveWip(){
        this.wips++;
    }

    public void play(){
        communicator.sendMessage("Playing " + name);
        communicator.sendMessage("your cards are: " + handCards);
        communicator.sendMessage("Cards on the table are: " + table.toString());

        if (build != null && !table.getDeck().contains(build)){// build has been stolen
            build = null;
        }

        String s = communicator.receiveMessage();

        List<CardInterface> cards = cardConverter.fromString(s.substring(2));
        CardInterface myCard = cards.getFirst();
        List<CardInterface> cardFromTable = cards.subList(1, cards.size());

        if (s.startsWith("B")) { // start building
            if (!isValidBuild(myCard, cardFromTable)) {
                return;
            }
            MetaCard newCard = new MetaCard(cards);
            table.giveCard(newCard);
            table.removeCards(cardFromTable);
            build = newCard;
        } else if (s.startsWith("M")){
            if (!cardCheck(null, cards)) {
                return;
            }
            table.removeCards(cards);
            MetaCard metaCard = new MetaCard(cards);
            table.giveCard(metaCard);
            metaCards.add(metaCard);
            play();
            return;
        } else if (s.startsWith("D")) {//drop on table
            if (!isValidDrop(myCard)) {return;}
            table.giveCard(myCard);
        } else if (s.startsWith("T")) {// take from table
            if (!isValidTake((Card) myCard, cardFromTable)) {return;}
            table.removeCards(cardFromTable);
            build = null;
            this.cardsWon.addAll(cardConverter.flatten(cards));

        } else failure("unrecognized input");
        metaCards.clear();
        handCards.removeCard(myCard);
    }

    private boolean cardCheck(CardInterface myCard, List<CardInterface> cardsFromTable){
        if (myCard != null && !handCards.getDeck().contains(myCard)) {
            failure("First card is not in you hand");
            return false;
        }

        if (cardsFromTable != null && !new HashSet<>(table.getDeck()).containsAll(cardsFromTable)) {
            failure("Card you want to use from table does not exit");
            return false;
        }
        return true;
    }

    private void failure(String message){
        communicator.sendMessage(message);
        table.removeCards(metaCards);
        table.giveCards(cardConverter.flatten(metaCards));
        metaCards.clear();
        play();
    }

    private boolean isValidDrop(CardInterface myCard){
        if (!cardCheck(myCard, null)) {return false;}
        boolean currentlyBuilding = build != null;
        if  (currentlyBuilding) {
            failure("You cannot drop while building");
        }
        return !currentlyBuilding;
    }

    private boolean isValidTake(Card myCard, List<CardInterface> cardsFromTable){
        if (!cardCheck(myCard, cardsFromTable)) {return false;}
        // all cards must be the same value
        if (!summingWithFaces(myCard, cardsFromTable)){return false;}

        if (build != null && !cardsFromTable.contains(build)) {
            failure("Take your build first!");
            return false;
        }
        int myCardValue = cardConverter.cardValue(myCard);
        boolean correct = cardsFromTable.stream()
                .map(cardConverter::cardValue)
                .allMatch(x -> x == myCardValue);

        if (!correct) {
            failure("invalid take");
        }
        return correct;
    }

    private boolean isValidBuild(CardInterface myCard, List<CardInterface> cardsFromTable) {
        if (!cardCheck(myCard, cardsFromTable)) {return false;}
        List<CardInterface> newlyBuild = new ArrayList<>();
        newlyBuild.add(myCard);
        newlyBuild.addAll(cardsFromTable);

        if (!summingWithFaces(myCard,newlyBuild)) {return false;}

        List<Integer> mcValuesList = newlyBuild.stream().map(cardConverter::cardValue).toList();
        int mcValues = mcValuesList.stream().reduce(0, Integer::sum);

        // we must have the card were building to
        boolean buildForSum = handCards.getDeck().stream()
                .map(cardConverter::cardValue)
                .anyMatch(x -> x==mcValues);

        boolean buildForRepetition = handCards.getDeck().stream()
                .map(cardConverter::cardValue)
                .anyMatch(x -> mcValuesList.stream().allMatch(y->y.equals(x)));

        if (!buildForSum && !buildForRepetition) {
            failure("Invalid sum");
        }
        return buildForSum || buildForRepetition;
    }

    private boolean summingWithFaces(CardInterface myCard, List<CardInterface> newlyBuild){
        if (cardConverter.isFace(myCard)) {
            boolean allFaces = cardConverter.flatten(newlyBuild).stream().allMatch(cardConverter::isFace);
            if (!allFaces) {
                failure("You try to sum with faces or try to sum higher then ten");
                return false;
            }
        }
        return true;
    }

    public Score calculateScore() {
        return new Score(cardsWon.size(),
                (int)  cardsWon.stream().filter(c -> c.suit().equals(Suit.Clubs)).count(),
                (int)  cardsWon.stream().filter(c -> c.value().equals(Value.Ace)).count(),
                wips,
                cardsWon.contains(new Card(Suit.Diamonds, Value.Ten)),
                cardsWon.contains(new Card(Suit.Clubs, Value.Two))
                );
    }
}
