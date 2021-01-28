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
package org.scify.talkandplay.gui.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ResourceManager;

public class TabsPanel extends javax.swing.JPanel {

    private User user;
    private ConfigurationPanel parent;
    private CommunicationTab communicationPanel;
    private EntertainmentTab entertainmentPanel;
    private GamesTab gamesPanel;
    protected ResourceManager rm;

    public TabsPanel(User user, ConfigurationPanel parent) {
        this.user = user;
        this.parent = parent;
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabsPanel = new javax.swing.JTabbedPane();

        setBackground(new java.awt.Color(255, 255, 255));

        tabsPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {

        communicationPanel = new CommunicationTab(user, parent);
        entertainmentPanel = new EntertainmentTab(user, parent);
        gamesPanel = new GamesTab(user, parent);

        tabsPanel.addTab(rm.getTextOfXMLTag("communicationName"), communicationPanel);
        tabsPanel.addTab(rm.getTextOfXMLTag("entertainmentName"), entertainmentPanel);
        tabsPanel.addTab(rm.getTextOfXMLTag("gamesName"), gamesPanel);

        tabsPanel.setSelectedIndex(0);
        tabsPanel.setForegroundAt(0, Color.white);
        tabsPanel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));

        tabsPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                for (int i = 0; i < tabsPanel.getTabCount(); i++) {
                    tabsPanel.setForegroundAt(i, Color.decode(UIConstants.green));
                }
                tabsPanel.setForegroundAt(tabsPanel.getSelectedIndex(), Color.white);

                if (tabsPanel.getSelectedIndex() == 0) {
                    parent.showInfoPanel();
                } else if (tabsPanel.getSelectedIndex() == 1) {
                    parent.hideInfoPanel();
                } else if (tabsPanel.getSelectedIndex() == 2) {
                    parent.showGamesPanel();
                }
            }
        });
    }

    public void redrawCategoriesList() {
        communicationPanel.redrawCategoriesList();
    }

    public void redrawCategoriesListWithOrder() {
        communicationPanel.redrawCategoriesListWithOrder();
    }

    public void stopPlayer() {
        gamesPanel.stopPlayer();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabsPanel;
    // End of variables declaration//GEN-END:variables
}
