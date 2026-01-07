package org.cardGames.cardsAPI.communication;

public interface Communicator {
    void sendMessage(String message);
    String receiveMessage();
}
