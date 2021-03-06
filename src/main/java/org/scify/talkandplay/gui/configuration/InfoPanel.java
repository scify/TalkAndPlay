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

import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.UserService;
import org.scify.talkandplay.utils.ResourceManager;

public class InfoPanel extends javax.swing.JPanel {

    private GuiHelper guiHelper;
    private ConfigurationPanel parent;
    private User user;
    private UserService userService;
    protected ResourceManager rm;

    public InfoPanel(ConfigurationPanel parent, User user) {
        this.guiHelper = new GuiHelper();
        this.parent = parent;
        this.user = user;
        this.userService = new UserService();
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

        jPanel1 = new javax.swing.JPanel();
        editExplLabel = new javax.swing.JLabel();
        addExplLabel = new javax.swing.JLabel();
        addWordButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        editLabel = new javax.swing.JLabel();
        addLabel = new javax.swing.JLabel();
        brokenFilesLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        brokenFilesPanel = new javax.swing.JPanel();
        //changeOrderLabel = new javax.swing.JLabel();
        //jScrollPane2 = new javax.swing.JScrollPane();
        //changeOrderTextArea = new javax.swing.JTextArea();
        //changeOrderButton = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(460, 655));

        editExplLabel.setText(rm.getTextOfXMLTag("editText"));
        String addWordButtonText = rm.getTextOfXMLTag("newWordButton");
        addExplLabel.setText(rm.getTextOfXMLTag("additionText") + " \"" + addWordButtonText + "\"");

        addWordButton.setBackground(new java.awt.Color(75, 161, 69));
        addWordButton.setFont(addWordButton.getFont());
        addWordButton.setForeground(new java.awt.Color(255, 255, 255));
        addWordButton.setText(addWordButtonText);
        addWordButton.setBorder(null);
        addWordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addWordButtonMouseClicked(evt);
            }
        });

        backButton.setBackground(new java.awt.Color(75, 161, 69));
        backButton.setFont(backButton.getFont());
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText(rm.getTextOfXMLTag("backButton"));
        backButton.setBorder(null);
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backButtonMouseClicked(evt);
            }
        });

        editLabel.setText("1. " + rm.getTextOfXMLTag("edit") + "  ");

        addLabel.setText("2. " + rm.getTextOfXMLTag("addition") + "  ");

        brokenFilesLabel.setText(rm.getTextOfXMLTag("communicationTabFilesNotFoundErrorMessage")+ ":");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        brokenFilesPanel.setBackground(new java.awt.Color(255, 255, 255));
        brokenFilesPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout brokenFilesPanelLayout = new javax.swing.GroupLayout(brokenFilesPanel);
        brokenFilesPanel.setLayout(brokenFilesPanelLayout);
        brokenFilesPanelLayout.setHorizontalGroup(
            brokenFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        brokenFilesPanelLayout.setVerticalGroup(
            brokenFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 129, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(brokenFilesPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editExplLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    //.addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editLabel)
                            .addComponent(addLabel)
                            .addComponent(addExplLabel)
                            //.addComponent(changeOrderLabel)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(addWordButton)
                                .addGap(18, 18, 18)
                                .addComponent(backButton)
                                .addGap(18, 18, 18))
                                //.addComponent(changeOrderButton)
                            .addComponent(brokenFilesLabel))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(editLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editExplLabel)
                .addGap(15, 15, 15)
                .addComponent(addLabel)
                .addGap(12, 12, 12)
                .addComponent(addExplLabel)
                .addGap(18, 18, 18)
                //.addComponent(changeOrderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                //.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addWordButton)
                    .addComponent(backButton))
                    //.addComponent(changeOrderButton)
                .addGap(49, 49, 49)
                .addComponent(brokenFilesLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addWordButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addWordButtonMouseClicked
        parent.addPanel(new WordFormPanel(user, null, parent));
    }//GEN-LAST:event_addWordButtonMouseClicked

    private void backButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonMouseClicked
        parent.goBack();
    }//GEN-LAST:event_backButtonMouseClicked

    private void changeOrderButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changeOrderButtonMouseClicked
         parent.redrawCategoriesListWithOrder();
    }//GEN-LAST:event_changeOrderButtonMouseClicked

    private void initCustomComponents() {
        setLayout(new GridLayout(0, 1, 0, 0));
        Font font = new Font(UIConstants.mainFont, Font.BOLD, 16);
        editLabel.setFont(font);
        addLabel.setFont(font);
        //changeOrderLabel.setFont(font);

        //changeOrderTextArea.setEditable(false);
        //changeOrderTextArea.setLineWrap(true);
        //changeOrderTextArea.setWrapStyleWord(true);
        //.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        //todo: remove
        //changeOrderLabel.setText("");
        //changeOrderTextArea.setText("");
        
        //jScrollPane2.setBorder(null);

        List<String> brokenFiles = userService.getBrokenFiles(user.getName());
        if (brokenFiles.size() == 0) {
            brokenFilesLabel.setVisible(false);
            jScrollPane1.setVisible(false);
            brokenFilesPanel.setVisible(false);
        } else {
            font = new Font(UIConstants.mainFont, Font.BOLD, 14);
            brokenFilesLabel.setFont(font);
            brokenFilesLabel.setVisible(true);
            jScrollPane1.setVisible(true);
            brokenFilesPanel.setVisible(true);
            brokenFilesPanel.setLayout(new BoxLayout(brokenFilesPanel, BoxLayout.Y_AXIS));

            for (String file : brokenFiles) {
                JLabel fileLabel = new JLabel(file);
                add(fileLabel);
                brokenFilesPanel.add(fileLabel);
            }
        }
        guiHelper.drawButton(addWordButton);
        guiHelper.drawButton(backButton);
        //guiHelper.drawButton(changeOrderButton);
        //changeOrderButton.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addExplLabel;
    private javax.swing.JLabel addLabel;
    private javax.swing.JButton addWordButton;
    private javax.swing.JButton backButton;
    private javax.swing.JLabel brokenFilesLabel;
    private javax.swing.JPanel brokenFilesPanel;
    //private javax.swing.JButton changeOrderButton;
    //private javax.swing.JLabel changeOrderLabel;
    //private javax.swing.JTextArea changeOrderTextArea;
    private javax.swing.JLabel editExplLabel;
    private javax.swing.JLabel editLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    //private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
