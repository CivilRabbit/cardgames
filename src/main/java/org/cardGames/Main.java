package org.cardGames;

import org.cardGames.cardsAPI.communication.TerminalCommunicator;
import org.cardGames.wippen.Wippen;

import javax.smartcardio.TerminalFactorySpi;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        Wippen wip  = new Wippen(2, new TerminalCommunicator());
        wip.startGame();


    }
}