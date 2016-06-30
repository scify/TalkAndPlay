package org.scify.talkandplay.gui.configuration;

import java.awt.Color;
import java.awt.Font;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;

public class TabsPanel extends javax.swing.JPanel {

    private User user;
    private ConfigurationPanel parent;
    private CommunicationPanel communicationPanel;
    private EntertainmentPanel entertainmentPanel;

    public TabsPanel(User user, ConfigurationPanel parent) {
        this.user = user;
        this.parent = parent;

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
            .addComponent(tabsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 822, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {

        communicationPanel = new CommunicationPanel(user, parent);
        entertainmentPanel = new EntertainmentPanel(user);

        tabsPanel.addTab("Επικοινωνία", communicationPanel);
        tabsPanel.addTab("Ψυχαγωγία", entertainmentPanel);
        tabsPanel.addTab("Παιχνίδια", null);

        tabsPanel.setSelectedIndex(0);
        tabsPanel.setForegroundAt(0, Color.white);
        tabsPanel.setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 18));

        tabsPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                for (int i = 0; i < tabsPanel.getTabCount(); i++) {
                    tabsPanel.setForegroundAt(i, Color.decode(UIConstants.getMainColor()));
                }
                tabsPanel.setForegroundAt(tabsPanel.getSelectedIndex(), Color.white);
            }
        });
    }

    public void redrawCategoriesList() {
        communicationPanel.redrawCategoriesList();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabsPanel;
    // End of variables declaration//GEN-END:variables
}
