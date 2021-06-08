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
package org.scify.talkandplay.gui.grid;

import java.awt.Color;
import org.scify.talkandplay.gui.grid.selectors.Selector;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ResourceManager;

public class BasePanel extends javax.swing.JPanel {

    protected GridFrame parent;
    protected User user;
    protected Selector selector;
    protected ResourceManager rm;

    public BasePanel(User user, GridFrame parent) {
        this.user = user;
        this.parent = parent;
        this.rm = ResourceManager.getInstance();
        setBackground(Color.white);
    }

    protected void showMainMenu() {
        GridPanel gridPanel = new GridPanel(user, parent);
    }

}
