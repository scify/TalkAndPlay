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
import org.scify.talkandplay.gui.grid.tiles.TilePanel;

/**
 * The main selector for the grid
 *
 * @author christina
 */
public class TileSelector extends Selector {

    protected int BORDER_SIZE = 5;

    public TileSelector(List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);
    }

    @Override
    public void setUnselected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, BORDER_SIZE));
        panelList.get(i).setBackground(Color.decode(defaultBackgroundColor));

        ((TilePanel) panelList.get(i).getComponent(0)).setUnSelected();
    }

    @Override
    public void setSelected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(BORDER_COLOR), BORDER_SIZE));
        panelList.get(i).setBackground(Color.decode(BACKGROUND_COLOR));

        ((TilePanel) panelList.get(i).getComponent(0)).setSelected();
    }
}
