package org.scify.talkandplay.gui;

import org.scify.talkandplay.utils.LoginManager;
import org.scify.talkandplay.utils.OperationMessage;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private JLabel minLengthLabel;
    private JLabel containsUppercaseLabel;
    private JLabel containsNumberLabel;
    private JLabel containsSymbolLabel;
    private final ResourceManager rm;
    private LoginManager loginManager;
    protected String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%+=])(?=\\S+$).{8,}";
    protected String containsNumberPattern = "(?=.*[0-9]).{1,}";
    protected String containsUpperCasePattern = "(?=.*[A-Z]).{1,}";
    protected String containsSymbolPattern = "(?=.*[!@#$%+=]).{1,}";

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
        minLengthLabel.setText(rm.getTextOfXMLTag("passwordInstructionsLabel1"));
        minLengthLabel.setForeground(Color.RED);
        containsUppercaseLabel.setText(rm.getTextOfXMLTag("passwordInstructionsLabel2"));
        containsUppercaseLabel.setForeground(Color.RED);
        containsNumberLabel.setText(rm.getTextOfXMLTag("passwordInstructionsLabel3"));
        containsNumberLabel.setForeground(Color.RED);
        containsSymbolLabel.setText(rm.getTextOfXMLTag("passwordInstructionsLabel4"));
        containsSymbolLabel.setForeground(Color.RED);

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

        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }
        });

        passwordField1.setTransferHandler(null);
        passwordField2.setTransferHandler(null);

        passwordField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }
        });

        passwordField2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                validateButtonRegister();
            }
        });

        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
            }
        });

        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
            }
        });

        passwordField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
            }
        });
    }

    protected void validateButtonRegister() {
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField1.getPassword()).trim();
        String passwordConfirmation = String.valueOf(passwordField2.getPassword()).trim();
        if (email.length() <= 5 || !email.contains("@") || !email.contains(".") || !password.equals(passwordConfirmation) || !password.matches(passwordPattern))
            buttonRegister.setEnabled(false);
        else
            buttonRegister.setEnabled(true);
        if (password.equals(passwordConfirmation)) {
            passwordField2.setBackground(Color.WHITE);
        } else {
            passwordField2.setBackground(Color.RED);
        }

        if (password.length() > 7)
            minLengthLabel.setForeground(Color.decode("#4BA145"));
        else
            minLengthLabel.setForeground(Color.RED);

        if (password.matches(containsUpperCasePattern))
            containsUppercaseLabel.setForeground(Color.decode("#4BA145"));
        else
            containsUppercaseLabel.setForeground(Color.RED);

        if (password.matches(containsNumberPattern))
            containsNumberLabel.setForeground(Color.decode("#4BA145"));
        else
            containsNumberLabel.setForeground(Color.RED);

        if (password.matches(containsSymbolPattern))
            containsSymbolLabel.setForeground(Color.decode("#4BA145"));
        else
            containsSymbolLabel.setForeground(Color.RED);

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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
