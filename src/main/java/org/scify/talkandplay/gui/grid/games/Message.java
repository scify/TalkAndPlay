/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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

import org.scify.talkandplay.utils.ResourceManager;

import java.util.Random;

/**
 *
 * @author christina
 */
public class Message {
    protected ResourceManager rm;
    protected static Random random = new Random();
    protected static Message instance;

    public Message() {
        rm = ResourceManager.getInstance();
    }


    public static Message getInstance() {
        if (instance == null)
            instance = new Message();
        return instance;
    }

    public String getRandomCongratsMessage() {
        String message1 = rm.getTextOfXMLTag("congratulationMessage1");
        String message2 = rm.getTextOfXMLTag("congratulationMessage2");
        String message3 = rm.getTextOfXMLTag("congratulationMessage3");
        String[] messages = {message1, message2, message3};
        return messages[random.nextInt(messages.length)];
    }

    public String getRandomMistakeMessage() {
        String message1 = rm.getTextOfXMLTag("mistakeMessage1");
        String message2 = rm.getTextOfXMLTag("mistakeMessage2");
        String[] messages = {message1, message2};
        return messages[random.nextInt(messages.length)];
    }
}
