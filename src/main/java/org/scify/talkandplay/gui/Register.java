package org.scify.talkandplay.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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
import java.awt.event.*;
import java.util.Locale;

public class Register extends JPanel {

    private MainFrame parent;
    private JPanel registerPanel;
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
        super();
        loginManager = TalkAndPlayProfileConfiguration.getInstance().getLoginManager();
        this.parent = parent;
        rm = ResourceManager.getInstance();
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(registerPanel, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(registerPanel)
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
        registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayoutManager(8, 3, new Insets(0, 0, 0, 0), -1, -1));
        registerPanel.setAutoscrolls(false);
        registerPanel.setBackground(new Color(-1));
        registerPanel.setForeground(new Color(-1));
        registerPanel.setMaximumSize(new Dimension(-1, -1));
        registerPanel.setMinimumSize(new Dimension(700, 300));
        registerPanel.setPreferredSize(new Dimension(-1, -1));
        emailLabel = new JLabel();
        emailLabel.setDoubleBuffered(true);
        Font emailLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, emailLabel.getFont());
        if (emailLabelFont != null) emailLabel.setFont(emailLabelFont);
        emailLabel.setText("Label");
        registerPanel.add(emailLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setDoubleBuffered(true);
        Font passwordLabelFont = this.$$$getFont$$$(null, Font.BOLD, 20, passwordLabel.getFont());
        if (passwordLabelFont != null) passwordLabel.setFont(passwordLabelFont);
        passwordLabel.setText("Label");
        registerPanel.add(passwordLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        passwordLabel2 = new JLabel();
        passwordLabel2.setDoubleBuffered(true);
        Font passwordLabel2Font = this.$$$getFont$$$(null, Font.BOLD, 20, passwordLabel2.getFont());
        if (passwordLabel2Font != null) passwordLabel2.setFont(passwordLabel2Font);
        passwordLabel2.setText("Label");
        registerPanel.add(passwordLabel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        passwordField2 = new JPasswordField();
        registerPanel.add(passwordField2, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        emailField = new JTextField();
        registerPanel.add(emailField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        passwordField1 = new JPasswordField();
        registerPanel.add(passwordField1, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        buttonRegister = new JButton();
        buttonRegister.setBackground(new Color(-11820731));
        buttonRegister.setDoubleBuffered(true);
        Font buttonRegisterFont = this.$$$getFont$$$(null, Font.BOLD, 16, buttonRegister.getFont());
        if (buttonRegisterFont != null) buttonRegister.setFont(buttonRegisterFont);
        buttonRegister.setForeground(new Color(-1));
        buttonRegister.setText("Button");
        registerPanel.add(buttonRegister, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        minLengthLabel = new JLabel();
        minLengthLabel.setBackground(new Color(-1));
        minLengthLabel.setText("Label");
        registerPanel.add(minLengthLabel, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        containsUppercaseLabel = new JLabel();
        containsUppercaseLabel.setBackground(new Color(-1));
        containsUppercaseLabel.setText("Label");
        registerPanel.add(containsUppercaseLabel, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        containsNumberLabel = new JLabel();
        containsNumberLabel.setBackground(new Color(-1));
        containsNumberLabel.setText("Label");
        registerPanel.add(containsNumberLabel, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        containsSymbolLabel = new JLabel();
        containsSymbolLabel.setBackground(new Color(-1));
        containsSymbolLabel.setText("Label");
        registerPanel.add(containsSymbolLabel, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 30), null, 0, false));
        buttonBack = new JButton();
        buttonBack.setBackground(new Color(-11820731));
        buttonBack.setDoubleBuffered(true);
        Font buttonBackFont = this.$$$getFont$$$(null, Font.BOLD, 16, buttonBack.getFont());
        if (buttonBackFont != null) buttonBack.setFont(buttonBackFont);
        buttonBack.setForeground(new Color(-1));
        buttonBack.setText("Button");
        registerPanel.add(buttonBack, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 30), null, 0, false));
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
        return registerPanel;
    }
}
