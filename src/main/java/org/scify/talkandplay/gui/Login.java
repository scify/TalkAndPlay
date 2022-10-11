package org.scify.talkandplay.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import io.sentry.Sentry;
import org.scify.talkandplay.services.UserService;
import org.scify.talkandplay.utils.LoginManager;
import org.scify.talkandplay.utils.OperationMessage;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Locale;
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
        super();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(loginPanel, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(loginPanel)
        );
        loginMode = true;
        loginManager = TalkAndPlayProfileConfiguration.getInstance().getLoginManager();
        this.parent = parent;
        rm = ResourceManager.getInstance();
        initLoginButton();
        initRegisterButton();
        initForgotPasswordButton();
        buttonSignIn.setEnabled(false);
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                validateButtonSignIn();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                validateButtonSignIn();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                validateButtonSignIn();
            }
        });
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
            }
        });
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                validateButtonSignIn();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                validateButtonSignIn();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                validateButtonSignIn();
            }
        });
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);
            }
        });

        emailLabel.setText(rm.getTextOfXMLTag("emailLabel"));
        passwordLabel.setText(rm.getTextOfXMLTag("passwordLabel"));
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
                        Sentry.capture(ex.getMessage());
                    }
                }
            }
        });
    }

    protected void validateButtonSignIn() {
        if (loginMode) {
            String email = emailField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();
            if (email.length() <= 3 || !email.contains("@") || !email.contains(".") || password.length() < 3)
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
        buttonSignIn.setText(rm.getTextOfXMLTag("loginButton"));
        buttonSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (loginMode) {
                    if (buttonSignIn.isEnabled()) {
                        String userNameBasedOnEMail = emailField.getText();
                        OperationMessage operationMessage = loginManager.signIn(userNameBasedOnEMail, String.valueOf(passwordField.getPassword()));
                        if (operationMessage.isSuccess()) {
                            switchToLogOutMode();
                            UserService us = new UserService(userNameBasedOnEMail);
                            /*try {

                                us.createFirstUserForSHAPESMode(userNameBasedOnEMail);
                            } catch (Exception ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                                Sentry.capture(ex.getMessage());
                            }*/
                            parent.loginAsRegisteredUser(userNameBasedOnEMail);
                        } else {
                            passwordField.setText("");
                            JOptionPane.showMessageDialog(null, operationMessage.getError(), "", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayoutManager(6, 5, new Insets(0, 0, 0, 0), -1, -1));
        loginPanel.setAutoscrolls(false);
        loginPanel.setBackground(new Color(-1));
        loginPanel.setForeground(new Color(-1));
        loginPanel.setMaximumSize(new Dimension(-1, -1));
        loginPanel.setMinimumSize(new Dimension(700, 266));
        loginPanel.setPreferredSize(new Dimension(-1, -1));
        emailLabel = new JLabel();
        emailLabel.setDoubleBuffered(true);
        Font emailLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, emailLabel.getFont());
        if (emailLabelFont != null) emailLabel.setFont(emailLabelFont);
        emailLabel.setText("Label");
        loginPanel.add(emailLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setDoubleBuffered(true);
        Font passwordLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, passwordLabel.getFont());
        if (passwordLabelFont != null) passwordLabel.setFont(passwordLabelFont);
        passwordLabel.setText("Label");
        loginPanel.add(passwordLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        emailField = new JTextField();
        loginPanel.add(emailField, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        buttonSignIn = new JButton();
        buttonSignIn.setBackground(new Color(-11820731));
        buttonSignIn.setDoubleBuffered(true);
        Font buttonSignInFont = this.$$$getFont$$$(null, Font.BOLD, 16, buttonSignIn.getFont());
        if (buttonSignInFont != null) buttonSignIn.setFont(buttonSignInFont);
        buttonSignIn.setForeground(new Color(-1));
        buttonSignIn.setText("Button");
        loginPanel.add(buttonSignIn, new GridConstraints(3, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        forgotPasswordLabel = new JLabel();
        forgotPasswordLabel.setDoubleBuffered(true);
        Font forgotPasswordLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, forgotPasswordLabel.getFont());
        if (forgotPasswordLabelFont != null) forgotPasswordLabel.setFont(forgotPasswordLabelFont);
        forgotPasswordLabel.setText("Label");
        loginPanel.add(forgotPasswordLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        noAccountLabel = new JLabel();
        noAccountLabel.setDoubleBuffered(true);
        Font noAccountLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, noAccountLabel.getFont());
        if (noAccountLabelFont != null) noAccountLabel.setFont(noAccountLabelFont);
        noAccountLabel.setText("Label");
        loginPanel.add(noAccountLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        buttonForgotPassword = new JButton();
        buttonForgotPassword.setBackground(new Color(-11820731));
        buttonForgotPassword.setDoubleBuffered(true);
        Font buttonForgotPasswordFont = this.$$$getFont$$$(null, Font.BOLD, 16, buttonForgotPassword.getFont());
        if (buttonForgotPasswordFont != null) buttonForgotPassword.setFont(buttonForgotPasswordFont);
        buttonForgotPassword.setForeground(new Color(-1));
        buttonForgotPassword.setText("Button");
        loginPanel.add(buttonForgotPassword, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRegister = new JButton();
        buttonRegister.setBackground(new Color(-11820731));
        buttonRegister.setDoubleBuffered(true);
        Font buttonRegisterFont = this.$$$getFont$$$(null, Font.BOLD, 16, buttonRegister.getFont());
        if (buttonRegisterFont != null) buttonRegister.setFont(buttonRegisterFont);
        buttonRegister.setForeground(new Color(-1));
        buttonRegister.setText("Button");
        loginPanel.add(buttonRegister, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return loginPanel;
    }
}
