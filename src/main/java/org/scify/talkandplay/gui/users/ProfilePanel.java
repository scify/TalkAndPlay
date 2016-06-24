package org.scify.talkandplay.gui.users;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import org.scify.talkandplay.gui.MainFrame;
import org.scify.talkandplay.gui.MainPanel;
import org.scify.talkandplay.gui.configuration.ConfigurationPanel;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.UserService;

public class ProfilePanel extends javax.swing.JPanel {

    private MainFrame parent;
    private User user;
    private GuiHelper guiHelper;
    private UserService userService;

    /**
     * Creates new form ProfilePanel
     */
    public User getUser() {
        return user;
    }

    public ProfilePanel() {
        initComponents();
    }

    public ProfilePanel(MainFrame mainFrame, User user) {
        this.parent = mainFrame;
        this.user = user;
        this.guiHelper = new GuiHelper();
        this.userService = new UserService();
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

        profilePanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        profilePanel.setBackground(new java.awt.Color(255, 255, 255));
        profilePanel.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        add(profilePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        JLabel imageLabel = new JLabel(guiHelper.getRoundIcon((user.getImage())));
        JLabel editLabel = new JLabel("Επεξεργασία");
        JLabel deleteLabel = new JLabel("Διαγραφή");
        JLabel configLabel = new JLabel("Config");
        JLabel nameLabel;

        if (userService.hasBrokenFiles(user)) {
            nameLabel = new JLabel(user.getName(), new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/warning.png")), JLabel.RIGHT);
        } else {
            nameLabel = new JLabel(user.getName());
        }

        editLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parent.changePanel(new UserFormPanel(parent, user));

            }
        });

        deleteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Είσαι σίγουρος/η ότι θες να διαγράψεις το χρήστη;", "Warning", 0);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        userService.delete(user);
                        parent.changePanel(new MainPanel(parent));
                    } catch (Exception ex) {
                        Logger.getLogger(ProfilePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
         configLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parent.changePanel(new ConfigurationPanel());

            }
        });

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        nameLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 16));
        editLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        editLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        editLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 11));
        editLabel.setForeground(Color.decode("#4BA145"));
        deleteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        deleteLabel.setFont(new Font("DejaVu Sans", Font.PLAIN, 11));
        deleteLabel.setForeground(Color.decode("#AE001D"));

        profilePanel.add(imageLabel);
        profilePanel.add(nameLabel);
        profilePanel.add(editLabel);
        profilePanel.add(deleteLabel);
        profilePanel.add(configLabel);
    }

    public void repaintPanel(User user) {
        /* nameLabel.setText(user.getName());
         imageLabel.setIcon(guiHelper.getIcon((user.getImage())));*/
    }

    public User getProfile() {
        return user;
    }

    public void setProfile(User user) {
        this.user = user;
    }

    public void update(String user) {
        //  nameLabel.setText(user);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel profilePanel;
    // End of variables declaration//GEN-END:variables
}
