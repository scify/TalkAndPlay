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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.sentry.Sentry;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.gui.users.UserPanel;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.CategoryService;
import org.scify.talkandplay.services.UserService;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;

public class CommunicationTab extends javax.swing.JPanel {

    private User user;
    private UserService userService;
    private CategoryService categoryService;
    private List<JPanel> panels;
    private ConfigurationPanel parent;
    private Font plainFont, boldFont;
    private ImageIcon editIcon, deleteIcon, upIcon, downIcon;
    private int row;
    protected ResourceManager rm;
    private GridBagConstraints c;

    private static final int MARGIN = 20;

    public CommunicationTab(User user, ConfigurationPanel parent) {
        this.user = user;
        this.parent = parent;
        this.userService = new UserService();
        this.categoryService = new CategoryService();
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        panels = new ArrayList();
        plainFont = new Font(UIConstants.mainFont, Font.PLAIN, 14);
        boldFont = new Font(UIConstants.mainFont, Font.BOLD, 14);
        editIcon = new ImageIcon(rm.getImage("edit-icon.png", ResourceType.JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        deleteIcon=new ImageIcon(rm.getImage("delete-icon.png", ResourceType.JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        upIcon=new ImageIcon(rm.getImage("up-icon-white.png", ResourceType.JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        downIcon=new ImageIcon(rm.getImage("down-icon-white.png", ResourceType.JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        
        
        //  setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        contentPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;

        row = 1;
        c.weightx = 1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = row;
        row++;

        setBorder(new EmptyBorder(20, 0, 0, 0));

        contentPanel.add(titlePanel(), c);

        drawCategories(user.getCommunicationModule().getCategories(), MARGIN, false);
    }

    /**
     * Recursively draw the categories
     *
     * @param categories
     * @param margin
     */
    private void drawCategories(List<Category> categories, int margin, boolean withOrdering) {
        if (categories == null || categories.isEmpty()) {
            return;
        } else {
            margin += MARGIN;

            for (Category category : categories) {
                final JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(Color.white);

                JPanel controlsPanel = new JPanel();

                JLabel label = new JLabel(category.getName());
                label.setBorder(new EmptyBorder(6, margin, 6, 0));

                JLabel editLabel = new JLabel(editIcon);
                JLabel deleteLabel = new JLabel(deleteIcon);
                editLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

                if (withOrdering) {
                    JLabel upLabel = new JLabel(upIcon);
                    JLabel downLabel = new JLabel(downIcon);
                    upLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
                    downLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

                    controlsPanel.add(upLabel);
                    controlsPanel.add(downLabel);

                    setOrderListeners(upLabel, downLabel, contentPanel.getComponentCount());
                }

                controlsPanel.add(editLabel);
                controlsPanel.add(deleteLabel);
                controlsPanel.setVisible(false);
                controlsPanel.setBackground(Color.decode(UIConstants.green));

                //bold font for the parent categories, plain for the children
                if (category.getSubCategories().size() > 0) {
                    label.setFont(boldFont);
                } else {
                    label.setFont(plainFont);
                }

                setListeners(panel, editLabel, deleteLabel, category.getName());

                panel.add(label, BorderLayout.LINE_START);
                panel.add(controlsPanel, BorderLayout.LINE_END);

                c.gridy = row;
                contentPanel.add(panel, c);
                panels.add(panel);
                row++;
                drawCategories(category.getSubCategories(), margin, withOrdering);
            }
            margin -= MARGIN;
        }
    }

    public void redrawCategoriesList() {
        panels = new ArrayList();
        contentPanel.removeAll();
        contentPanel.add(titlePanel(), c);

        user = userService.getUser(user.getName());
        drawCategories(user.getCommunicationModule().getCategories(), MARGIN, false);

        revalidate();
        repaint();
    }

    public void redrawCategoriesListWithOrder() {
        contentPanel.removeAll();
        contentPanel.add(titlePanel(), c);

        user = userService.getUser(user.getName());
        drawCategories(user.getCommunicationModule().getCategories(), MARGIN, true);

        //set the first panel as the selected one
        if (panels.size() > 0) {
            panels.get(2).setBackground(Color.decode(UIConstants.green));
            panels.get(2).getComponent(0).setForeground(Color.white);
            panels.get(2).getComponent(1).setVisible(true);
        }

        panels.get(2).setBackground(Color.decode(UIConstants.green));
        panels.get(2).getComponent(0).setForeground(Color.white);
        panels.get(2).getComponent(1).setVisible(true);
        revalidate();
        repaint();
    }

    private JPanel titlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        panel.setBackground(Color.white);
        JLabel label = new JLabel(user.getCommunicationModule().getName());
        label.setFont(new Font(UIConstants.mainFont, Font.BOLD, 16));

        panel.add(label);

        return panel;
    }

    private void setListeners(final JPanel panel, JLabel editLabel, JLabel deleteLabel, final String category) {
        //add the green background when a panel is clicked
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {

                for (JPanel panel : panels) {
                    panel.setBackground(Color.white);
                    panel.getComponent(0).setForeground(Color.black);
                    panel.getComponent(1).setVisible(false);
                }
                
                panel.setBackground(Color.decode(UIConstants.green));
                panel.getComponent(0).setForeground(Color.white);
                panel.getComponent(1).setVisible(true);
            }
        });

        editLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                parent.addPanel(new WordFormPanel(user, categoryService.getCategory(category, user), parent));
            }
        });

        deleteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 1) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, rm.getTextOfXMLTag("wordDeleteConfirmation"), "Warning", 0);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try {
                            categoryService.delete(category, user);
                            redrawCategoriesList();
                            parent.redrawCategoriesDropDown();
                        } catch (Exception ex) {
                            Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
                            Sentry.capture(ex);
                        }
                    }
                }
            }
        });
    }

    private void setOrderListeners(JLabel upLabel, JLabel downLabel, final int i) {
        upLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Component[] components = contentPanel.getComponents();
                contentPanel.removeAll();
                Component temp = components[i];
                components[i] = components[i - 1];
                components[i - 1] = temp;

                c.gridy = 1;
                for (Component comp : components) {
                    c.gridy++;
                    contentPanel.add(comp, c);
                }
                contentPanel.validate();
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(contentPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
