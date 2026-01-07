package org.cardGames.cardsAPI.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalCommunicator implements Communicator {


    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String receiveMessage() {
        String message = "";
        try {
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(System.in));
            message = r.readLine();
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return message;
    }
}
