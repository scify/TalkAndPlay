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
package org.scify.talkandplay.gui.grid.selectors;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Selector for the grey buttons found at the media player and the games
 *
 * @author christina
 */
public class ButtonSelector extends Selector {

    protected int BORDER_SIZE = 5;

    public ButtonSelector(List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);
    }

    @Override
    public void setUnselected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, getBorderSize()));
        panelList.get(i).setBackground(Color.decode(defaultBackgroundColor));
    }

    @Override
    public void setSelected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(BORDER_COLOR), getBorderSize()));
        panelList.get(i).setBackground(Color.decode(BACKGROUND_COLOR));
    }

    public int getBorderSize() {
        return BORDER_SIZE;
    }
}
