package org.cardGames.cardsAPI.communication;

import java.util.List;

public class TestCommunicator implements Communicator {
    private final List<String> instructions;
    private int index = 0;
    public TestCommunicator(List<String> instructions) {
        this.instructions = instructions;
    }

    @Override
    public void sendMessage(String message) {}

    @Override
    public String receiveMessage() {
        return instructions.get(index++);
    }
}
