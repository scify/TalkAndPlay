package org.scify.talkandplay.gui;

import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.utils.LoginManager;
import org.scify.talkandplay.utils.OperationMessage;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private final ResourceManager rm;
    private LoginManager loginManager;

    public Register(MainFrame parent) {
        loginManager = TalkAndPlayProfileConfiguration.getInstance().getLoginManager();
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
        buttonRegister.setEnabled(false);

        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                validateButtonRegister();
            }
        });

        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                validateButtonRegister();
            }
        });

        passwordField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                validateButtonRegister();
            }
        });
    }

    protected void validateButtonRegister() {
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField1.getPassword()).trim();
        String passwordConfirmation = String.valueOf(passwordField2.getPassword()).trim();
        if (email.length() <= 3 || !email.contains("@") || !password.equals(passwordConfirmation) || password.length() < 4)
            buttonRegister.setEnabled(false);
        else
            buttonRegister.setEnabled(true);
        if (password.equals(passwordConfirmation)) {
            passwordField2.setBackground(Color.WHITE);
        } else {
            passwordField2.setBackground(Color.RED);
        }

    }

    protected void initButtonRegister() {
        buttonRegister.setText(rm.getTextOfXMLTag("registerButton"));
        buttonRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (buttonRegister.isEnabled()) {
                    String email = emailField.getText();
                    String password = String.valueOf(passwordField1.getPassword());
                    String passwordConfirmation = String.valueOf(passwordField2.getPassword());
                    OperationMessage operationMessage = loginManager.signUp(email, password, passwordConfirmation);
                    if (operationMessage.isSuccess()) {
                        JOptionPane.showMessageDialog(null, rm.getTextOfXMLTag("successfulRegistrationMsg"), "", JOptionPane.INFORMATION_MESSAGE);
                        parent.goToLogin();
                    } else {
                        passwordField1.setText("");
                        passwordField2.setText("");
                        JOptionPane.showMessageDialog(null, operationMessage.getError(), "", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
