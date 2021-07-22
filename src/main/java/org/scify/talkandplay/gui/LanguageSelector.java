package org.scify.talkandplay.gui;

import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LanguageSelector extends javax.swing.JPanel {

    private MainFrame parent;
    private JPanel flagsPanel;
    private JPanel closePanel;
    private final ResourceManager rm;
    private JPanel selectedFlagPanel;

    public LanguageSelector(MainFrame parent) {
        this.parent = parent;
        rm = ResourceManager.getInstance();
        int numberOfLanguages = rm.getNumberOfAvailableLanguages();
        flagsPanel = new javax.swing.JPanel();
        flagsPanel.setBackground(new java.awt.Color(255, 255, 255));
        flagsPanel.setForeground(new java.awt.Color(255, 255, 255));
        flagsPanel.setLayout(new GridLayout(0, numberOfLanguages + 2));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(flagsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(flagsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(Color.white);
        flagsPanel.add(emptyPanel);

        Border notFocusBorder = BorderFactory.createLineBorder(Color.white, 5);
        Border focusBorder = BorderFactory.createLineBorder(Color.decode("#00aa44"), 5);
        Border selectedBorder = BorderFactory.createDashedBorder(Color.decode("#00aa44"), 3, 5, 2, true);
        Border notSelectedBorder = new EmptyBorder(3, 3, 3, 3);
        for (int i = 0; i < numberOfLanguages; i++) {
            final int languageId = i;
            JPanel flagPanel = new JPanel();
            flagPanel.setLayout(new BoxLayout(flagPanel, BoxLayout.Y_AXIS));
            flagPanel.setBackground(Color.white);
            if (languageId == rm.getSelectedLanguageIndex()) {
                flagPanel.setBorder(selectedBorder);
                selectedFlagPanel = flagPanel;
            } else
                flagPanel.setBorder(notSelectedBorder);
            JLabel flagLabel = new javax.swing.JLabel();
            flagLabel.setIcon(rm.getImageIconOfLanguage("flag.png", i));
            flagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            flagLabel.setBorder(notFocusBorder);
            flagPanel.add(flagLabel);
            flagsPanel.add(flagPanel);

            flagLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent arg0) {
                    flagLabel.setBorder(notFocusBorder);
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                    flagLabel.setBorder(focusBorder);

                }

                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (selectedFlagPanel != flagPanel) {
                        selectedFlagPanel.setBorder(notSelectedBorder);
                        flagPanel.setBorder(selectedBorder);
                        selectedFlagPanel = flagPanel;
                        rm.setLanguage(languageId);

                    } else {
                        parent.languageSelected((new Date ()).getTime());
                                            }
                }
            });
        }
        initCloseButton();
    }

    protected void initCloseButton() {
        closePanel = new JPanel();
        JLabel closeLabel = new JLabel();
        closeLabel.setIcon(new ImageIcon(rm.getImage("close-icon.png", ResourceType.JAR).getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        closeLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        closePanel.setLayout(new BoxLayout(closePanel, BoxLayout.Y_AXIS));
        closePanel.setBackground(Color.white);
        closePanel.setAlignmentY(Component.TOP_ALIGNMENT);
        closePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        closePanel.add(closeLabel);
        flagsPanel.add(closePanel);
        closePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                parent.languageSelected((new Date ()).getTime());
            }
        });

    }
}
