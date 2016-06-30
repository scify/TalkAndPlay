package org.scify.talkandplay.gui.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.gui.users.ProfilePanel;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.CategoryService;
import org.scify.talkandplay.services.UserService;

public class CommunicationPanel extends javax.swing.JPanel {

    private User user;
    private UserService userService;
    private CategoryService categoryService;
    private List<JPanel> panels;
    private ConfigurationPanel parent;

    private static final int MARGIN = 20;

    public CommunicationPanel(User user, ConfigurationPanel parent) {
        this.user = user;
        this.parent = parent;
        this.userService = new UserService();
        this.categoryService = new CategoryService();

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        panels = new ArrayList();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(20, 0, 0, 0));

        add(titlePanel());

        drawCategories(user.getCommunicationModule().getCategories(), MARGIN);
    }

    /**
     * Recursively draw the categories
     *
     * @param categories
     * @param margin
     */
    private void drawCategories(List<Category> categories, int margin) {
        if (categories == null || categories.isEmpty()) {
            return;
        } else {
            margin += MARGIN;
            for (Category category : categories) {
                final JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(Color.white);

                JLabel label = new JLabel(category.getName());
              //  label.setBorder(new EmptyBorder(0, margin, 0, 0));

                JLabel editLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/edit-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                JLabel deleteLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/delete-icon.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                editLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

                JPanel controlsPanel = new JPanel();
                controlsPanel.add(editLabel);
                controlsPanel.add(deleteLabel);
                controlsPanel.setVisible(false);
                controlsPanel.setBackground(Color.decode(UIConstants.getMainColor()));

                if (category.getSubCategories().size() > 0) {
                    label.setFont(new Font(UIConstants.getMainFont(), Font.BOLD, 14));
                } else {
                    label.setFont(new Font(UIConstants.getMainFont(), Font.PLAIN, 14));
                }

                setListeners(panel, editLabel, deleteLabel, category.getName());

                panel.add(label, BorderLayout.LINE_START);
                panel.add(controlsPanel, BorderLayout.LINE_END);
                add(panel);
                panels.add(panel);

                drawCategories(category.getSubCategories(), margin);
            }
            margin -= MARGIN;
        }
    }

    public void redrawCategoriesList() {
        removeAll();

        add(titlePanel());

        user = userService.getUser(user.getName());
        drawCategories(user.getCommunicationModule().getCategories(), MARGIN);

        revalidate();
        repaint();
    }

    private JPanel titlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        panel.setBackground(Color.white);
        JLabel label = new JLabel(user.getCommunicationModule().getName());
        label.setFont(new Font(UIConstants.getMainFont(), Font.BOLD, 16));

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
                panel.setBackground(Color.decode(UIConstants.getMainColor()));
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
                int dialogResult = JOptionPane.showConfirmDialog(null, "Είσαι σίγουρος/η ότι θες να διαγράψεις τη λέξη;", "Warning", 0);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        categoryService.delete(category, user);
                        redrawCategoriesList();
                    } catch (Exception ex) {
                        Logger.getLogger(ProfilePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
