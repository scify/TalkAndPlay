package org.scify.talkandplay.gui;

import io.sentry.Sentry;
import org.scify.talkandplay.utils.LoginManager;
import org.scify.talkandplay.utils.OperationMessage;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends JPanel {

    private MainFrame parent;
    private JPanel loginPanel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel forgotPasswordLabel;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton buttonSignIn;
    private JLabel noAccountLabel;
    private JButton buttonRegister;
    private JButton buttonForgotPassword;
    private final ResourceManager rm;
    private LoginManager loginManager;
    private boolean loginMode;

    public Login(MainFrame parent) {
        loginMode = true;
        loginManager = TalkAndPlayProfileConfiguration.getInstance().getLoginManager();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.parent = parent;
        rm = ResourceManager.getInstance();
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        emailLabel.setText(rm.getTextOfXMLTag("emailLabel"));
        passwordLabel.setText(rm.getTextOfXMLTag("passwordLabel"));
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(loginPanel, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(loginPanel)
        );
        initLoginButton();
        initRegisterButton();
        initForgotPasswordButton();
        buttonSignIn.setEnabled(false);
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                validateButtonSignIn();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
                validateButtonSignIn();
            }
        });
    }

    protected void initForgotPasswordButton() {
        forgotPasswordLabel.setText(rm.getTextOfXMLTag("forgotPasswordLabel"));
        buttonForgotPassword.setText(rm.getTextOfXMLTag("forgotPasswordButton"));
        buttonForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://kubernetes.pasiphae.eu/shapes/asapa/auth/password/recovery"));
                    } catch (Exception ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        Sentry.capture(ex);
                    }
                }
            }
        });
    }

    protected void validateButtonSignIn() {
        if (loginMode) {
            String email = emailField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();
            if (email.length() <= 3 || !email.contains("@") || password.length() < 4)
                buttonSignIn.setEnabled(false);
            else
                buttonSignIn.setEnabled(true);
        }
    }

    public void switchToLogOutMode() {
        loginMode = false;
        emailField.setEditable(false);
        passwordField.setEditable(false);
        buttonRegister.setVisible(false);
        noAccountLabel.setVisible(false);
        buttonSignIn.setText(rm.getTextOfXMLTag("logoutButton"));
    }

    public void switchToLogInMode() {
        loginMode = true;
        buttonSignIn.setText(rm.getTextOfXMLTag("loginButton"));
        emailField.setEditable(true);
        emailField.setText("");
        passwordField.setEditable(true);
        passwordField.setText("");
        buttonRegister.setVisible(true);
        noAccountLabel.setVisible(true);
    }

    protected void initLoginButton() {
        Login login = this;
        buttonSignIn.setText(rm.getTextOfXMLTag("loginButton"));
        buttonSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (loginMode) {
                    if (buttonSignIn.isEnabled()) {
                        OperationMessage operationMessage = loginManager.signIn(emailField.getText(), String.valueOf(passwordField.getPassword()));
                        if (operationMessage.isSuccess()) {
                            switchToLogOutMode();
                            parent.loginAsRegisteredUser(login);
                        } else {
                            passwordField.setText("");
                            JOptionPane.showMessageDialog(null, operationMessage.getError(), "", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else{
                    switchToLogInMode();
                    parent.logoutAsRegisteredUser();
                }
            }
        });
    }

    protected void initRegisterButton() {
        noAccountLabel.setText(rm.getTextOfXMLTag("noAccountLabel"));
        buttonRegister.setText(rm.getTextOfXMLTag("registerButton"));
        buttonRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                parent.goToRegister();
            }
        });
    }

}
