/**
* Copyright 2016 SciFY
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
    private static final String[] ERROR = {"Λάθος. Προσπάθησε ξανά!", "Προσπάθησε ξανά."};
    private static Random random = new Random();

    public static String getRandomCongrats() {
        return CONGRATS[random.nextInt(CONGRATS.length)];
    }

    public static String getRandomError() {
        return ERROR[random.nextInt(ERROR.length)];
    }
}
