/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.games;

import java.util.Random;

/**
 *
 * @author christina
 */
public class Message {

    private static final String[] CONGRATS = {"Μπράβο!", "Τα κατάφερες!", "Σωστά!"};
    private static final String[] ERROR = {"Λάθος. Προσπάθησε ξανά!"};
    private Random random = new Random();

    public String getRandomCongrats() {
        return CONGRATS[random.nextInt(CONGRATS.length)];
    }

    public String getRandomError() {
        return ERROR[random.nextInt(ERROR.length)];
    }
}
