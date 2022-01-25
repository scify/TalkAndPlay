package org.scify.talkandplay.gui;

import org.scify.talkandplay.utils.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Register extends JPanel {

    private MainFrame parent;
    private JPanel loginPanel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField1;
    private JTextField emailField;
    private JButton buttonRegister;
    private JLabel passwordLabel2;
    private JPasswordField passwordField2;
    private JButton buttonBack;
    private JPanel closePanel;
    private final ResourceManager rm;

    public Register(MainFrame parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.parent = parent;
        rm = ResourceManager.getInstance();
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        emailLabel.setText(rm.getTextOfXMLTag("emailLabel"));
        passwordLabel.setText(rm.getTextOfXMLTag("passwordLabel"));
        passwordLabel2.setText(rm.getTextOfXMLTag("passwordConfirmationLabel"));


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(loginPanel, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(loginPanel)
        );
        initButtonRegister();
        initButtonBack();
    }

    protected void initButtonRegister() {
        buttonRegister.setText(rm.getTextOfXMLTag("registerButton"));
        buttonRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                parent.goToRegister();
            }
        });
    }

    protected void initButtonBack() {
        buttonBack.setText(rm.getTextOfXMLTag("backButton"));
        buttonBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                parent.goToLogin();
            }
        });
    }
}
