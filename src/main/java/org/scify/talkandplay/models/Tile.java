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
package org.scify.talkandplay.models;

import javax.swing.JPanel;

public class Tile {

    private JPanel panel;
    private boolean hasManualListener;

    public Tile() {

    }

    public Tile(JPanel panel, boolean hasManualListener) {
        this.panel = panel;
        this.hasManualListener = hasManualListener;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public boolean hasManualListener() {
        return hasManualListener;
    }

    public void setHasManualListener(boolean hasManualListener) {
        this.hasManualListener = hasManualListener;
    }

}
